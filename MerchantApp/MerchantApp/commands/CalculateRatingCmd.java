package MerchantApp.commands;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

public class CalculateRatingCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int intItemID;
		StringBuffer errorBuffer = new StringBuffer();
		JsonObject error = new JsonObject();
		intItemID = Integer.parseInt((String) mapUserData.get("itemID"));
		System.out.println("CALCULATE RATING COMMAND");
		if (intItemID <= 0) {
		
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}

		sqlProc = connection.prepareCall("{call calculateRating(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, intItemID);
		sqlProc.execute();
		System.out.println("avg: " + sqlProc.getInt(1));
		StringBuffer sb = new StringBuffer();
		
		sb.append(sqlProc.getInt(1));

//		ResultSet results = (ResultSet) sqlProc.getObject(1);
//		ResultSetMetaData metaData = results.getMetaData();
//		int count = metaData.getColumnCount();
		
		if(!sb.equals(null) ){
			System.out.println("Success!");
			strbufResult = makeJSONResponseEnvelope(200, null, sb);
			sqlProc.close();
			return strbufResult;
		}else{
			System.out.println("Error:  No such item in DB!");
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			sqlProc.close();
			return errorBuffer;
		}
		
//		strbufResult = makeJSONResponseEnvelope(200, null, sb);
//		sqlProc.close();
//
//		return strbufResult;
	}
}