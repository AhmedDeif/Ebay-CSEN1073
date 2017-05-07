package UserApp.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import UserApp.server.Cache;
import UserApp.server.ClientHandle;
import UserApp.server.ClientRequest;
import UserApp.server.Dispatcher;
import UserApp.server.ParseListener;
import UserApp.server.RequestParser;

class Controller implements ParseListener {

	protected Dispatcher _dispatcher;
	protected ExecutorService _threadPoolParsers;

	public Controller() {
	}

	public void init() throws Exception {
		_dispatcher = new Dispatcher();
		_dispatcher.init();
		_threadPoolParsers = Executors.newFixedThreadPool(10);
	}

	public void execRequest(ClientHandle clientHandle) {
		_threadPoolParsers.execute(new RequestParser(this, clientHandle));
	}

	public synchronized void parsingFinished(ClientHandle clientHandle, ClientRequest clientRequest) {
		try {
			String strAction;
			strAction = clientRequest.getAction();
			if ( strAction.equalsIgnoreCase("createUser")
					|| strAction.equalsIgnoreCase("getUser") 
					|| strAction.equalsIgnoreCase("getUserCommand")
					|| strAction.equalsIgnoreCase("addAddressCommand")
					 || strAction.equalsIgnoreCase("getUserCommand")) {
				_dispatcher.dispatchRequest(clientHandle, clientRequest);
			} else {
				String strSessionID;
				strSessionID = clientRequest.getSessionID();
				if (strSessionID == null || strSessionID.length() == 0 || !Cache.sessionExists(strSessionID)) {
					clientHandle.terminateClientRequest();

				} else {
					_dispatcher.dispatchRequest(clientHandle, clientRequest);
				}
			}
		} catch (Exception exp) {
			clientHandle.terminateClientRequest();
			System.err.println(exp.toString());
		}
	}

	public synchronized void parsingFailed(ClientHandle clientHandle, String strError) {
		clientHandle.terminateClientRequest();
		System.err.println("An error in parsing " + strError);
	}

}