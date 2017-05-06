package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

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
		StringBuffer sb = new StringBuffer();
//		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
//		sqlProc.close();
		
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
