package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;


public class DeleteCategoryCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int intID;

		// intID = (int)mapUserData.get( "ID" );
		intID = Integer.parseInt((String) mapUserData.get("id"));

		if (intID <= 0)
			return null;

		sqlProc = connection.prepareCall("{?=call deleteCategory(?)}");
		sqlProc.registerOutParameter(1, Types.VARCHAR);
		sqlProc.setInt(2, intID);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getString(1));
		System.out.println(sqlProc.getString(1));
		strbufResult = makeJSONResponseEnvelope(1, null, sb);
		String collectionName = sqlProc.getString(1);


		sqlProc.close();


		return strbufResult;
	}
}