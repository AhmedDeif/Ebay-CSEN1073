package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import redis.clients.jedis.Jedis;

public class AddUserCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String firstName, lastName, email, password, gender;

		firstName = (String) mapUserData.get("firstName");
		lastName = (String) mapUserData.get("lastName");
		email = (String) mapUserData.get("email");
		password = (String) mapUserData.get("password");
		// gender = (String) mapUserData.get("gender");

		// if (firstName == null || strItemName.trim().length() == 0 || dblPrice
		// <= 0 || intQuantity <= 0
		// || intSellerID <= 0)
		// return null;
		System.out.println("nameeee:  " + firstName);

		sqlProc = connection.prepareCall("{call addUser(?,?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(1, firstName);
		sqlProc.setString(2, lastName);
		sqlProc.setString(3, email);
		sqlProc.setString(4, password);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));

		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);

	

		Jedis jedis = new Jedis("localhost");
		jedis.set("user_id", "" + sqlProc.getInt(1));
		sqlProc.close();
		return strbufResult;
	}

}
