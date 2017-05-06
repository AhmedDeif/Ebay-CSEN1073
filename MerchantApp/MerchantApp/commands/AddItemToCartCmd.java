package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

//// Cart Item Cmd
public class AddItemToCartCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intCartID,
                            intItemID,
                            intQuantity;
                            
        intCartID    =   Integer.parseInt((String) mapUserData.get( "cartID" ));
        intItemID    =   Integer.parseInt((String) mapUserData.get( "itemID" ));
        intQuantity    =   Integer.parseInt((String) mapUserData.get( "quantity" ));

        if(intCartID <= 0 || intItemID <= 0 || intQuantity <= 0 ) {
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}

        sqlProc = connection.prepareCall("{call AddItemToCart(?,?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intCartID);
        sqlProc.setInt(2, intItemID);
        sqlProc.setInt(3, intQuantity);

        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        System.out.println("--------------");
        System.out.println(sb.toString());
    
        if(!sb.toString().equals(null) ){
        	System.out.println("Success!");
        	   strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
               sqlProc.close( );
               return strbufResult;
        }else{
        	System.out.println("ERROR!");
        	StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			sqlProc.close( );
			return errorBuffer;
        	
        }
        
     

        
    }
}