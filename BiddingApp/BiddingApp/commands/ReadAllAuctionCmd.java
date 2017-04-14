package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import redis.clients.jedis.Jedis;


public class ReadAllAuctionCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int pUID;
		
		Jedis jedis = new Jedis("localhost");
		if (jedis.get("user_id") != null)
			pUID = Integer.parseInt(jedis.get("user_id"));
		else
			pUID = -1;
		
//		pUID = (int) mapUserData.get("pUID");
		
		sqlProc = connection.prepareCall("{?=call getAuctionsForUser(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(2, pUID);
		
		sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}

}
