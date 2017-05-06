package UserApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;
import java.util.Calendar;
import java.util.Map;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

public class UpdateUserCommand extends Command implements Runnable {

  public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
    // SQL - NoSQL
	  StringBuffer strbufResult;
	  CallableStatement sqlProc;
	  int id = Integer.parseInt((String) mapUserData.get("id"));
//	  Jedis jedis = new Jedis("localhost");
//	  int id = Integer.parseInt(jedis.get("user_id"));
	  String email = (String) mapUserData.get("email");
	  String password = (String) mapUserData.get("password");
	  String firstName = (String) mapUserData.get("firstName");
	  String lastName = (String) mapUserData.get("lastName");
	  String gender = (String) mapUserData.get("gender");
	  String dob = (String) mapUserData.get("dateOfBirth");
	  
	  if (email == null || email.trim().length() == 0 ||
			  password == null || password.trim().length() == 0 ||
			  firstName == null || firstName.trim().length() == 0 ||
			  lastName == null || lastName.trim().length() == 0 ||
			  gender == null || gender.trim().length() == 0 ||
			  dob == null || dob.trim().length() == 0) {
		  StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
	  }
	  
	  sqlProc = connection.prepareCall("{call updateUser(?,?,?,?,?,?,?)}");
	  sqlProc.registerOutParameter(1, Types.INTEGER);
	  sqlProc.setInt(1, id);
	  sqlProc.setString(2, firstName);
	  sqlProc.setString(3, lastName);
	  sqlProc.setString(4, email);
	  sqlProc.setString(5, password);
	  sqlProc.setString(6, gender);
//	  sqlProc.setString(7, dob);
	  sqlProc.setDate(7, new Date(2017, 4, 29), Calendar.getInstance());
	  
	  sqlProc.execute();
	  StringBuffer sb = new StringBuffer();
	  sb.append(sqlProc.getInt(1));
	  strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
	  sqlProc.close();
	
	  return strbufResult;
  }

}
