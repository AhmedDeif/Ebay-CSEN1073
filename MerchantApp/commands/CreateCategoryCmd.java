package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

///////// RANAAAAAA//////////////////////
//// Category Cmd
class CreateCategoryCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strCategoryName;

		strCategoryName = (String) mapUserData.get("categoryName");

		if (strCategoryName == null || strCategoryName.trim().length() == 0)
			return null;

		sqlProc = connection.prepareCall("{call createCategory(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(1, strCategoryName);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		sqlProc.close();

		return strbufResult;
	}
}