package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;


public class DeleteAuctionCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int pID;
		
		pID = Integer.parseInt(mapUserData.get("pID").toString());
		
		sqlProc = connection.prepareCall("{call deleteAuction(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, pID);
		
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
