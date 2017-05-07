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
public class EditCategoryCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strCategoryName;
		int intID;
		String oldName;

		strCategoryName = (String) mapUserData.get("categoryName");
		intID = Integer.parseInt((String) mapUserData.get("id"));
		// System.out.println("category name " + strCategoryName);
		// System.out.println("id " + intID);

		if (strCategoryName == null || strCategoryName.trim().length() == 0 || intID <= 0)
		{
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}
		sqlProc = connection.prepareCall("{call editCategory(?,?)}");
		sqlProc.registerOutParameter(1, Types.VARCHAR);
		sqlProc.setString(2, strCategoryName);
		sqlProc.setInt(1, intID);

		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getString(1));
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