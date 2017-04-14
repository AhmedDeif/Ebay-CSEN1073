package MessagesApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class GetThreadCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		Jedis jedis = new Jedis("localhost");
		int UserOneID = Integer.parseInt(jedis.get("user_id"));
//		  int UserOneID = Integer.parseInt((String) mapUserData.get("UserOneID"));
		  int UserTwoID = Integer.parseInt((String) mapUserData.get("UserTwoID"));
		  sqlProc = connection.prepareCall("{call updateUser(?,?,?,?,?,?,?,?)}");
		  sqlProc.registerOutParameter(1, Types.INTEGER);
		  sqlProc.setInt (1,UserOneID);
		  sqlProc.setInt(2, UserTwoID);
		  
		  sqlProc.execute();
		  StringBuffer sb = new StringBuffer();
		  sb.append(sqlProc.getInt(1));
		  strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		  
		  
		  sqlProc.close();
		  
		
		return strbufResult;
	}
	
}
