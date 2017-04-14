package MerchantApp.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import MerchantApp.server.Cache;
import MerchantApp.server.ClientHandle;
import MerchantApp.server.ClientRequest;
import MerchantApp.server.Dispatcher;
import MerchantApp.server.ParseListener;
import MerchantApp.server.RequestParser;

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
			if (strAction.equalsIgnoreCase("deleteItemCategory") || strAction.equalsIgnoreCase("createItem") 
					|| strAction.equalsIgnoreCase("createCart") || strAction.equalsIgnoreCase("deleteCart") ||
					strAction.equalsIgnoreCase("findCart") || strAction.equalsIgnoreCase("addItemToCart")
					|| strAction.equalsIgnoreCase("updateItemInCart") || strAction.equalsIgnoreCase("deleteItemInCart")
					|| strAction.equalsIgnoreCase("viewItemsInCart") || strAction.equalsIgnoreCase("editItem") ||
					strAction.equalsIgnoreCase("deleteItem")||strAction.equalsIgnoreCase("findItem") ||
					strAction.equalsIgnoreCase("viewItem") || strAction.equalsIgnoreCase("createComment") || 
					strAction.equalsIgnoreCase("editComment") || strAction.equalsIgnoreCase("viewComment") ||
					strAction.equalsIgnoreCase("findComment") || strAction.equalsIgnoreCase("createUserRating")
					|| strAction.equalsIgnoreCase("editUserRating") || strAction.equalsIgnoreCase("deleteUserRating")
					|| strAction.equalsIgnoreCase("viewItemUserRating")|| strAction.equalsIgnoreCase("findItemRating") 
					|| strAction.equalsIgnoreCase("calculateRating")|| strAction.equalsIgnoreCase("createCategory")
					|| strAction.equalsIgnoreCase("editCategory") || strAction.equalsIgnoreCase("createItemCategory")  
					|| strAction.equalsIgnoreCase("deleteCategory") || strAction.equalsIgnoreCase("findCategory")
					|| strAction.equalsIgnoreCase("findItemCategory") || strAction.equalsIgnoreCase("viewCategory")
					|| strAction.equalsIgnoreCase("createUser")
					) {
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