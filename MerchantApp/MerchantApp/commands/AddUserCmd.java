package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;
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

		if (firstName == null || lastName.trim().length() == 0 || email.trim().length() <= 0
				|| password.trim().length() <= 0) {
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
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
