package MerchantApp.server;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import MerchantApp.server.ServerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public class ClientHandle {

//	protected HttpRequest _httpRequest;
	protected String _messageRequest;
	protected ChannelHandlerContext _ctx;
	protected ServerHandler _serviceHandler;

	public ClientHandle(ChannelHandlerContext ctx, String messageRequest, ServerHandler serviceHandler) {
		_ctx = ctx;
		_messageRequest = messageRequest;
		_serviceHandler = serviceHandler;
	}

	public ChannelHandlerContext getContext() {
		return _ctx;
	}

	public String getRequest() {
		return _messageRequest ;
	}

	public ServerHandler getServiceHandler() {
		return _serviceHandler;
	}

	public void passResponsetoClient(StringBuffer strbufResponse) {

		_serviceHandler.setResponse(strbufResponse);
		synchronized (_serviceHandler) {
			_serviceHandler.notify();
		}
	}

	public void terminateClientRequest() {

		passResponsetoClient(null);
	}

	public String getClientIP() {
		String strIPAddress;

		InetSocketAddress socketAddress = (InetSocketAddress) _ctx.channel().remoteAddress();
		InetAddress inetaddress = socketAddress.getAddress();
		strIPAddress = inetaddress.getHostAddress();
		return strIPAddress;
	}
	
}