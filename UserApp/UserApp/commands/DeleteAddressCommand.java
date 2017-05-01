package UserApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class DeleteAddressCommand extends Command implements Runnable {

  public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
	  StringBuffer strbufResult;
	  CallableStatement sqlProc;
	  int UserID = Integer.parseInt((String) mapUserData.get("userid"));
//	  Jedis jedis = new Jedis("localhost");
//	  UserID = Integer.parseInt(jedis.get("user_id"));
		
      int addressID = Integer.parseInt((String) mapUserData.get("addressid"));
    
	  sqlProc = connection.prepareCall("{call deleteaddress(?,?)}");
	  sqlProc.registerOutParameter(1, Types.INTEGER);
	  sqlProc.setInt(1,UserID);
	  sqlProc.setInt(2,addressID);
	  
	  sqlProc.execute();
	  StringBuffer sb = new StringBuffer();
	  sb.append(sqlProc.getInt(1));
	  strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
	  sqlProc.close();
	 	  
	  return strbufResult;
  }

}
