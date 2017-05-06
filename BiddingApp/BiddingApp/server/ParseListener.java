package BiddingApp.server;

import BiddingApp.server.ClientHandle;
import BiddingApp.server.ClientRequest;

public interface ParseListener{

    public abstract void parsingFinished(  ClientHandle  clientHandle,
                                           ClientRequest clientRequest );

    public abstract void parsingFailed( ClientHandle clientHandle, String strError );

}
