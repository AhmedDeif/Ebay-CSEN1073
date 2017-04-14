package MerchantApp.server;

import MerchantApp.server.ClientHandle;
import MerchantApp.server.ClientRequest;

public interface ParseListener{

    public abstract void parsingFinished(  ClientHandle  clientHandle, 
                                           ClientRequest clientRequest );

    public abstract void parsingFailed( ClientHandle clientHandle, String strError );
    
}