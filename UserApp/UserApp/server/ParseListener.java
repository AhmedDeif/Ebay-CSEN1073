package UserApp.server;

import UserApp.server.ClientHandle;
import UserApp.server.ClientRequest;

public interface ParseListener{

    public abstract void parsingFinished(  ClientHandle  clientHandle, 
                                           ClientRequest clientRequest );

    public abstract void parsingFailed( ClientHandle clientHandle, String strError );
    
}