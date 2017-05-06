package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;


public class CreateCartCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intUserID;
                       
        System.out.println("Create Cart Cmd");
        
        intUserID    =   Integer.parseInt((String) mapUserData.get( "userID") );

        if(intUserID <= 0){
        	StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
        }
           

        sqlProc = connection.prepareCall("{call createCart(?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intUserID);
        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        System.out.println("RESULT:");
        System.out.println(sb.toString() );
        strbufResult = makeJSONResponseEnvelope( 200 , null, sb );
        sqlProc.close( );

        return strbufResult;
    }
}