package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;
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
			return null;

		sqlProc = connection.prepareCall("{call deleteItem(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, intItemID);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		sqlProc.close();

		// Mongo connection
		// To connect to mongodb server
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		// Now connect to your databases
		DB db = mongoClient.getDB("EbaySearch");
		System.out.println("Connect to database successfully");
		// insert item in mongoDb
		DBCollection coll = db.getCollection("items");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("itemId", intItemID);
		coll.remove(whereQuery);
		return strbufResult;

	}
}