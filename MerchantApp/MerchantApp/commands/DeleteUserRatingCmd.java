package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

public class DeleteUserRatingCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intItemID,
                            intUserID;

        
        
        intItemID =   Integer.parseInt((String)mapUserData.get( "itemID" ));
        intUserID =   Integer.parseInt((String)mapUserData.get( "userID" ));

        
//    	Jedis jedis = new Jedis("localhost");
//		if (jedis.get("user_id") != null)
//			intUserID = Integer.parseInt(jedis.get("user_id"));
//		else
//			intUserID = -1;

        if(intItemID <= 0 || intUserID <= 0)
        {
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
        sqlProc = connection.prepareCall("{call deleteUserRating(?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intItemID);
        sqlProc.setInt(2, intUserID);
        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
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