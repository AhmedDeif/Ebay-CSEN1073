package MessagesApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

public class GetMessage extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		
		  StringBuffer strbufResult;
		  CallableStatement sqlProc;
		  int threadID = Integer.parseInt((String) mapUserData.get("threadID"));
		  sqlProc = connection.prepareCall("{call updateUser(?,?,?,?,?,?,?,?)}");
		  sqlProc.registerOutParameter(1, Types.INTEGER);
		  sqlProc.setInt(1, threadID);
		  sqlProc.execute();
		  StringBuffer sb = new StringBuffer();
		  sb.append(sqlProc.getInt(1));
		  strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		  sqlProc.close();
		
		  
			
		  return strbufResult;
	}
	
	
	
}
