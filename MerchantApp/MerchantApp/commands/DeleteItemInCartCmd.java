package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

public class DeleteItemInCartCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intCartID,
                            intItemID;
                            
        intCartID    =   Integer.parseInt((String) mapUserData.get( "cartID" ));
        intItemID    =   Integer.parseInt((String) mapUserData.get( "itemID" ));

        if(intCartID <= 0 ||
            intItemID <= 0 )
    	{
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
        sqlProc = connection.prepareCall("{call deleteItemInCart(?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intCartID);
        sqlProc.setInt(2, intItemID);

        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        System.out.println("-----------");
		System.out.println(sb.toString());
		if (!sb.toString().equals(null)) {
			strbufResult = makeJSONResponseEnvelope(200, null, sb);
			sqlProc.close();

			return strbufResult;
		} else {
			sqlProc.close();
			System.out.println("DB returned null!");
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
    }
}