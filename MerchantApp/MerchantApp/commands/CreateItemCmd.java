package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.bulk.WriteRequest.Type;

import redis.clients.jedis.Jedis;

///////////////// START Nesreen ////////////////////////////
//// Item Cmds Nesreen
public class CreateItemCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strItemName, strDecription;
		int intQuantity, intCategoryID, nSQLResult, intSellerID, dblPrice;

		strItemName = (String) mapUserData.get("itemName");
		strDecription = (String) mapUserData.get("description");
		intQuantity = Integer.parseInt((String) mapUserData.get("quantity"));

		Jedis jedis = new Jedis("localhost");
		if (jedis.get("user_id") != null)
			intSellerID = Integer.parseInt(jedis.get("user_id"));
		else
			intSellerID = -1;
		
		jedis.close();
		// intSellerID = Integer.parseInt((String) mapUserData.get("sellerID"));
		intCategoryID = Integer.parseInt((String) mapUserData.get("categoryID"));

		dblPrice = Integer.parseInt((String) mapUserData.get("price"));

		if (strItemName == null || strItemName.trim().length() == 0 || dblPrice <= 0 || intQuantity <= 0
				|| intSellerID <= 0)
			return null;

		sqlProc = connection.prepareCall("{call createitem(?,?,?,?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(1, strItemName);
		sqlProc.setInt(2, dblPrice);
		sqlProc.setString(3, strDecription);
		sqlProc.setInt(4, intCategoryID);
		sqlProc.setInt(5, intQuantity);
		sqlProc.setInt(6, intSellerID);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		int itemId = sqlProc.getInt(1);
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		System.out.println("@@@@ " + sqlProc.getInt(1));
		sqlProc.close();

		sqlProc = connection.prepareCall("{call findcategoryname(?)}");
		sqlProc.registerOutParameter(1, Types.VARCHAR);
		sqlProc.setInt(1, intCategoryID);
		sqlProc.execute();
		String categoryName = sqlProc.getString(1);
		sqlProc.close();

		// Mongo connection
		// To connect to mongodb server
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		// Now connect to your databases
		DB db = mongoClient.getDB("EbaySearch");
		System.out.println("Connect to database successfully");

		// insert item in mongoDb
		DBCollection coll = db.getCollection("items");
		System.out.println("Collection mycol selected successfully");

		BasicDBObject doc = new BasicDBObject("itemName", strItemName).append("price", dblPrice)
				.append("description", strDecription).append("quantity", intQuantity).append("seller", intSellerID)
				.append("categoryName", categoryName).append("itemId", itemId);

		coll.insert(doc);

		return strbufResult;
	}
}
