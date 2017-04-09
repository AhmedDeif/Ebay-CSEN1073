package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

//// Cart Cmd
class CreateCartCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int intUserID;

		intUserID = Integer.parseInt((String) mapUserData.get("userID"));

		if (intUserID <= 0)
			return null;

		sqlProc = connection.prepareCall("{call createCart(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, intUserID);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		sqlProc.close();

		return strbufResult;
	}
}