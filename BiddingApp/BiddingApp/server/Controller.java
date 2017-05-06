package BiddingApp.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import BiddingApp.server.Cache;
import BiddingApp.server.ClientHandle;
import BiddingApp.server.ClientRequest;
import BiddingApp.server.Dispatcher;
import BiddingApp.server.ParseListener;
import BiddingApp.server.RequestParser;

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
			if (strAction.equalsIgnoreCase("createBid")  || strAction.equalsIgnoreCase("deleteAuction")
					|| strAction.equalsIgnoreCase("createAuction") || strAction.equalsIgnoreCase("editAuction")
					|| strAction.equalsIgnoreCase("readAllAuction") || strAction.equalsIgnoreCase("readAuction")) {
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