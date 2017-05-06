package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

public class createBidCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
		System.out.println("here");
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		
//		int strUserId, strItemId;
		int strBidAmount;
		int strUserId, strItemId;
	

		strUserId = Integer.parseInt(mapUserData.get("user_id").toString()) ;


		strItemId = Integer.parseInt(mapUserData.get("item_id").toString()) ;
		strBidAmount = Integer.parseInt((String)mapUserData.get("bid_amount"));
		sqlProc = connection.prepareCall("{call createbid(?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1,  strUserId);
		sqlProc.setInt(2,  strItemId);
		sqlProc.setInt(3, strBidAmount);

		System.out.println("-------");
		sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();
		System.out.println("-------");

		return strbufResult;
	}
	
}
