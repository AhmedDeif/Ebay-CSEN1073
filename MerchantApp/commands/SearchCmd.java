package commands;

import java.sql.Connection;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class SearchCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult = null;
		String searchTxt ;
		searchTxt = (String) mapUserData.get("search_txt");
		// Mongo connection
		// To connect to mongodb server
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		// Now connect to your databases
		DB db = mongoClient.getDB("EbaySearch");
		System.out.println("Connect to database successfully");
		DBCollection coll = db.getCollection("items");

		DBObject findCommand = new BasicDBObject("$text", new BasicDBObject("$search", searchTxt));

		DBObject projectCommand = new BasicDBObject("score", new BasicDBObject("$meta", "textScore"));

		DBObject sortCommand = new BasicDBObject("score", new BasicDBObject("$meta", "textScore"));
		DBCursor result = coll.find(findCommand, projectCommand).sort(sortCommand);
		
//		System.out.println("==> " + findCommand.toString());
//		System.out.println("==> " + projectCommand.toString());
//		System.out.println("==> " + sortCommand.toString());

		StringBuffer sb = new StringBuffer();
		
		
		System.out.println("###" + result.count());
		sb.append(result.one());
		while(result.hasNext()){
			sb.append(result.next());

		}

		System.out.println("Collection mycol selected successfully");
		
		strbufResult = makeJSONResponseEnvelope(1, null, sb);


		return strbufResult;
	}

	
}

