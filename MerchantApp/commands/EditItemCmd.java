package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class EditItemCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strItemName, strDecription;
		int intQuantity, intItemID, intSellerID, dblPrice;

		strItemName = (String) mapUserData.get("itemName");
		strDecription = (String) mapUserData.get("description");
		intQuantity = Integer.parseInt((String) mapUserData.get("quantity"));
		intSellerID = Integer.parseInt((String) mapUserData.get("sellerID"));
		intItemID = Integer.parseInt((String) mapUserData.get("itemID"));
		dblPrice = Integer.parseInt((String) mapUserData.get("price"));

		if (strItemName == null || strItemName.trim().length() == 0 || dblPrice <= 0 || intQuantity <= 0
				|| intSellerID <= 0 || intItemID <= 0)
			return null;

		sqlProc = connection.prepareCall("{call editItem(?,?,?,?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, intItemID);
		sqlProc.setString(2, strItemName);
		sqlProc.setInt(3, dblPrice);
		sqlProc.setString(4, strDecription);
		sqlProc.setInt(5, intQuantity);
		sqlProc.setInt(6, intSellerID);

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
		System.out.println("Collection mycol selected successfully");

		// db.items.find({"seller" : {"id":1}})
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("itemId", intItemID);
		BasicDBObject updateDocument = new BasicDBObject();
		updateDocument.put("itemName", strItemName);
		updateDocument.put("price", dblPrice);
		updateDocument.put("description", strDecription);
		updateDocument.put("quantity", intQuantity);
		updateDocument.put("seller", new BasicDBObject("id", intSellerID));
		coll.update(whereQuery, updateDocument);

		return strbufResult;
	}
}