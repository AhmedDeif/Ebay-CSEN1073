package UserApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class UpdateUserCommand extends Command implements Runnable {

  public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
    // SQL - NoSQL
	  StringBuffer strbufResult;
	  CallableStatement sqlProc;
//	  int id = Integer.parseInt((String) mapUserData.get("id"));
	  Jedis jedis = new Jedis("localhost");
	  int id = Integer.parseInt(jedis.get("user_id"));
	  String email = (String) mapUserData.get("email");
	  String password = (String) mapUserData.get("password");
	  String firstName = (String) mapUserData.get("firstName");
	  String lastName = (String) mapUserData.get("lastName");
	  String gender = (String) mapUserData.get("gender");
	  String phone = (String) mapUserData.get("phone");
	  String picturePath = ((String) mapUserData.get("picturePath"));
	  
	  sqlProc = connection.prepareCall("{call updateUser(?,?,?,?,?,?,?,?)}");
	  sqlProc.registerOutParameter(1, Types.INTEGER);
	  sqlProc.setInt(1, id);
	  sqlProc.setString(2, email);
	  sqlProc.setString(3, password);
	  sqlProc.setString(4, firstName);
	  sqlProc.setString(5, lastName);
	  sqlProc.setString(6, gender);
	  sqlProc.setString(7, phone);
	  sqlProc.setString(8, picturePath);
	  
	  sqlProc.execute();
	  StringBuffer sb = new StringBuffer();
	  sb.append(sqlProc.getInt(1));
	  strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
	  sqlProc.close();
	
	  return strbufResult;
  }

}
