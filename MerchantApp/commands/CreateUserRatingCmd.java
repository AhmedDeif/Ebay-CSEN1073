package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

//// Rating Cmd Nesreen
class CreateUserRatingCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int intItemID, intUserID, intRating;

		intItemID = Integer.parseInt((String) mapUserData.get("itemID"));
		intUserID = Integer.parseInt((String) mapUserData.get("userID"));
		intRating = Integer.parseInt((String) mapUserData.get("rating"));

		if (intItemID <= 0 || intUserID <= 0 || intRating <= 0)
			return null;

		sqlProc = connection.prepareCall("{call createUserRating(?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, intUserID);
		sqlProc.setInt(2, intItemID);
		sqlProc.setInt(3, intRating);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		sqlProc.close();

		return strbufResult;
	}
}