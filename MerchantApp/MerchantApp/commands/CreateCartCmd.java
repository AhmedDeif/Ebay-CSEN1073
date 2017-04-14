package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import redis.clients.jedis.Jedis;

//// Cart Cmd

class CreateCartCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intUserID;
                            
//        intUserID    =   Integer.parseInt((String) mapUserData.get( "userID") );
    	Jedis jedis = new Jedis("localhost");
		if (jedis.get("user_id") != null)
			intUserID = Integer.parseInt(jedis.get("user_id"));
		else
			intUserID = -1;
        
        
//    	intUserID = Integer.parseInt(jedis.get("user_id"));
        if(intUserID <= 0)
           return null;

        sqlProc = connection.prepareCall("{call createCart(?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intUserID);
        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
        sqlProc.close( );

        return strbufResult;
    }
}