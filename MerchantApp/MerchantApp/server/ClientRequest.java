package MerchantApp.server;


import java.util.Map;

public class ClientRequest {

	protected String _strAction, _strSessionID;
	protected Map<String, Object> _mapRequestData;
	protected Map<String, Object> _mapRequestProperties;

	public ClientRequest(String strAction, String strSessionID, Map<String, Object> mapRequestData, Map<String, Object>  mapRequestProperties) {
		_strAction = strAction;
		_strSessionID = strSessionID;
		_mapRequestData = mapRequestData;
		_mapRequestProperties = mapRequestProperties;
	}

	public String getAction() {
		return _strAction;
	}

	public String getSessionID() {
		return _strSessionID;
	}

	public Map<String, Object> getData() {
		return _mapRequestData;
	}
	
	public Map<String, Object> getProperties() {
		return _mapRequestProperties;
	}

}