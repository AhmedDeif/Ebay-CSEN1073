

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

public class RequestParser implements Runnable {

	protected ParseListener _parseListener;
	protected ClientHandle _clientHandle;

	public RequestParser(ParseListener parseListener, ClientHandle clientHandle) {
		_parseListener = parseListener;
		_clientHandle = clientHandle;
	}

	public void run() {
		try {
			HttpRequest request = _clientHandle.getRequest();

			System.out.println("STARTING PARSEING REQUEST........");
			if (request.method().compareTo(HttpMethod.POST) == 0) {
				System.out.println("Post Request Parser........");
				HttpPostRequestDecoder postDecoder;
				List<InterfaceHttpData> lst;

				postDecoder = new HttpPostRequestDecoder(request);
				Map<String, Object> bodyMap;
				
				
				List<InterfaceHttpData> httpList = postDecoder.getBodyHttpDatas();
				for (InterfaceHttpData temp : httpList) {
					if (temp instanceof Attribute) {
						Attribute requestData = (Attribute) temp;
						String requestDataValue = requestData.getValue();
						bodyMap = new Gson().fromJson(requestDataValue , Map.class);
						System.out.println("DATA MAP: "+ bodyMap);
						String action = bodyMap.get("action").toString();
						Map<String, Object> data = (Map<String, Object>) bodyMap.get("data"); 
//						String sessionId = bodyMap.get("sessionId").toString();
						ClientRequest _clientRequest = new ClientRequest(action, null , data);
						
						_parseListener.parsingFinished(_clientHandle, _clientRequest);

					}
				 }
			}

		} catch (Exception exp) {
			_parseListener.parsingFailed(_clientHandle, "Exception while parsing JSON object " + exp.toString());
		}
	}

}