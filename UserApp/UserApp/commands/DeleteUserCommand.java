package UserApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class DeleteUserCommand extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
		// SQL - NoSQL
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		// int id = Integer.parseInt((String) mapUserData.get("id"));
		Jedis jedis = new Jedis("localhost");
		int id = Integer.parseInt(jedis.get("user_id"));

		sqlProc = connection.prepareCall("{call deleteUser(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, id);

		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		sqlProc.close();

		return strbufResult;
	}

}
