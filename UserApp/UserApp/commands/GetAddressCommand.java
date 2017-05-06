package UserApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

public class GetAddressCommand extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
		StringBuffer strbufResult = null;
		CallableStatement sqlProc;
		int UserID = Integer.parseInt((String) mapUserData.get("userid"));
		// Jedis jedis = new Jedis("localhost");
		// int UserID = Integer.parseInt(jedis.get("user_id"));
		
		
		int AddressID = Integer.parseInt((String) mapUserData.get("addressid"));
		if (AddressID <= 0) {
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
		
        connection.setAutoCommit(false);
		sqlProc = connection.prepareCall("{call getaddress(?,?)}");
        sqlProc.registerOutParameter(1,  Types.OTHER);
		sqlProc.setInt(1, UserID);
		sqlProc.setInt(2, AddressID);

		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
//		sb.append(sqlProc.getInt(1));
//		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		System.out.println("executing done");
		/////
		ResultSet results = (ResultSet) sqlProc.getObject(1);
		System.out.println("results done" + results);

		ResultSetMetaData metaData = results.getMetaData();
		System.out.println("metaData done");

		int count = metaData.getColumnCount();
		System.out.println("Count = " + count);
//		int l = 0;
		
//		while (results.next()) {
//
//			for (int i = 1; i <= count ; i++) {
//				System.out.println(metaData.getColumnName(i) + " : " + results.getString(i) + ",");
//
//			}
//			sb.append(metaData.getColumnName(count) + " : " + results.getString(count) + ",");
//
//			System.out.println(results.getRow());
//			System.out.println("Count " + count);
//		}
//		System.out.println("loop done");
		
//		if (results.next()|| count >=1 ){
			System.out.println("gowa el if ");
			results.next();
			sb.append(metaData.getColumnName(1) + " : " + results.getString(0) + ",");
			strbufResult = makeJSONResponseEnvelope(1, null, sb);
			System.out.println("khvdxkdsgxz");


			results.close();
			sqlProc.close();

//		}
			if (!sb.toString().equals(null)) {
				strbufResult = makeJSONResponseEnvelope(200, null, sb);
//				sqlProc.close();

				return strbufResult;
			} else {
//				sqlProc.close();
				System.out.println("DB returned null!");
				StringBuffer errorBuffer = new StringBuffer();
				JsonObject error = new JsonObject();
				error.addProperty("errorMsg", "error");
				errorBuffer.append(error.toString());
				return errorBuffer;
			}


		/////
	}

}
