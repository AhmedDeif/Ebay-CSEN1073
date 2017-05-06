package SearchApp.commands;

import java.sql.Connection;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import SearchApp.commands.Command;

public class SearchCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult = null;
		String searchTxt ;
		searchTxt = (String) mapUserData.get("search_txt");
		
		System.out.println("search txt #################### " + searchTxt );

		// Mongo connection
		// To connect to mongodb server
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		// Now connect to your databases
		DB db = mongoClient.getDB("EbaySearch");
		System.out.println("Connect to database successfully");
		DBCollection coll = db.getCollection("items");
		System.out.println("Collection created ####################" );

		DBObject findCommand = new BasicDBObject("$text", new BasicDBObject("$search", searchTxt));

		System.out.println("Collection created  find ####################" + findCommand);

		DBObject projectCommand = new BasicDBObject("score", new BasicDBObject("$meta", "textScore"));
		System.out.println("projectCommand ####################" +projectCommand );

		DBObject sortCommand = new BasicDBObject("score", new BasicDBObject("$meta", "textScore"));
		 DBObject temp= new BasicDBObject("itemName", 4).append("description", 2).append("categoryName", 1);

		DBObject index = (new BasicDBObject("itemName","text").append("description", "text").append("categoryName", "text"));
		
		
		System.out.println("sortCommand ####################" + index );
		
//		coll.ensureIndex(new BasicDBObject("url", 1), new BasicDBObject("unique", true));
		coll.createIndex(index);
		
//		db.items.createIndex({"itemName":"text","description":"text","categoryName":"text"},{"weights":{itemName:4,description:2 , categoryName: 1}})
		
		
		DBCursor result = coll.find(findCommand, projectCommand).sort(sortCommand);
		
//		System.out.println("==> " + findCommand.toString());
//		System.out.println("==> " + projectCommand.toString());
//		System.out.println("==> " + sortCommand.toString());

		StringBuffer sb = new StringBuffer();
		
		System.out.println("SEARch###" + result.count());
//		String s = result.one().toString();
//		sb.append(s);
//		System.out.println(s);
		
		String n ="{\"res\":[";
		
		while(result.hasNext()){
			
			n += result.next().toString() + ",";
			System.out.println(n);
//			sb.append(n );

		}
		
		n = n.substring(0, n.length()-1);
		n += "]}";
		System.out.println(n);
		sb.append(n);

		System.out.println("Collection mycol selected successfully");
		

		if (!sb.equals(null)) {
			strbufResult = makeJSONResponseEnvelope(200, null, sb);
			return strbufResult;
		} else {
			System.out.println("Mongo Error!");
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
		
		

	}

	
}

