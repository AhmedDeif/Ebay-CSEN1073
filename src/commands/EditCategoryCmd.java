package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

class EditCategoryCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strCategoryName;
		int intID;

		strCategoryName = (String) mapUserData.get("categoryName");
		intID = Integer.parseInt((String) mapUserData.get("id"));
		// System.out.println("category name " + strCategoryName);
		// System.out.println("id " + intID);

		if (strCategoryName == null || strCategoryName.trim().length() == 0 || intID <= 0)
			return null;

		sqlProc = connection.prepareCall("{call editCategory(?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(2, strCategoryName);
		sqlProc.setInt(1, intID);

		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		sqlProc.close();

		return strbufResult;
	}
}