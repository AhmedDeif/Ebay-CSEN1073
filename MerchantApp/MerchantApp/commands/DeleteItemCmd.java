package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.zaxxer.hikari.HikariDataSource;
public class DeleteItemCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int intItemID;

		intItemID = Integer.parseInt((String) mapUserData.get("itemID"));

		if (intItemID <= 0)
		{
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
		sqlProc = connection.prepareCall("{call deleteItem(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, intItemID);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		System.out.println("-----------");
		System.out.println(sb.toString());
		sqlProc.close();
		
		if (!sb.toString().equals(null)) {
			strbufResult = makeJSONResponseEnvelope(200, null, sb);
			// To connect to mongodb server
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			// Now connect to your databases
			DB db = mongoClient.getDB("EbaySearch");
			System.out.println("Connected to database successfully");
			// insert item in mongoDb
			DBCollection coll = db.getCollection("items");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("itemId", intItemID);
			coll.remove(whereQuery);

			return strbufResult;
		} else {
		
			System.out.println("DB returned null!");
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}



	}
}