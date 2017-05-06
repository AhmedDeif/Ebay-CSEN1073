package SearchApp.server;


import java.util.HashMap;
import java.util.Map;


public class ResponseCodes {

    protected static final Map<String,String>   _mapCodes;
    
    static{
        _mapCodes = new HashMap<String, String>();
        String[][] pairs = {
        
            {"0","cheer up. you are good :)"},
            {"-1","false"},{"200","Request successful"},{"404","Not Found"},{"500","Internal Server Error"}
            

        };
        for (String[] pair : pairs) {
            _mapCodes.put( pair[0], pair[1] );
        }
    }
       
    public static String getMessage( String strCode ){
        return (String)_mapCodes.get( strCode );   
    }
}