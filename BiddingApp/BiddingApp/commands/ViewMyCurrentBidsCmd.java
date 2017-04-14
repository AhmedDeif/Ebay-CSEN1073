package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import redis.clients.jedis.Jedis;


public class ViewMyCurrentBidsCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int userID;
		
		

		Jedis jedis = new Jedis("localhost");
		if (jedis.get("user_id") != null)
			userID = Integer.parseInt(jedis.get("user_id"));
		else
			userID = -1;
		
		
//		userID = (int) mapUserData.get("userID");
		sqlProc = connection.prepareCall("{?=call viewMyCurrentBids(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(2, userID);

		
		sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}

}
