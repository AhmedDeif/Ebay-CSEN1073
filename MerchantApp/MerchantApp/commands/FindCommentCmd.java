package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

public class FindCommentCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult,strbufResponseJSON;
    
        CallableStatement   sqlProc;
        int                 intCommentID,
        	                     nSQLResult;

        intCommentID =   Integer.parseInt((String)mapUserData.get("commentID"));

        if(intCommentID <= 0 )
        {
			StringBuffer errorBuffer = new StringBuffer();
			JsonObject error = new JsonObject();
			error.addProperty("errorMsg", "error");
			errorBuffer.append(error.toString());
			return errorBuffer;
		}        
        connection.setAutoCommit(false);
        sqlProc = connection.prepareCall("{call findComment(?)}");
        sqlProc.registerOutParameter(1, Types.OTHER );
        sqlProc.setInt(1, intCommentID);
        sqlProc.execute( );
        ResultSet results = (ResultSet) sqlProc.getObject(1);
        ResultSetMetaData metaData = results.getMetaData();
        StringBuffer sb = new StringBuffer();
        int count = metaData.getColumnCount();
        int rows = results.getRow();

        System.out.println("SQL Result:");
		System.out.println("Rows: " + results.getRow());
		System.out.println("Count = " + count);
		System.out.println("-----------");
		JsonObject data = new JsonObject();
		while (results.next()) {

			for (int i = 1; i <= count; i++) {
				System.out.println(results.getString(i));
				data.addProperty(metaData.getColumnName(i), results.getString(i));
			}
			sb.append(data.toString());

			System.out.println(results.getRow());
			System.out.println("Count = " + count);
		}

		System.out.println("-----------");
		System.out.println(sb.toString());
		if (rows > 0) {
			strbufResult = makeJSONResponseEnvelope(200, null, sb);
			sqlProc.close();
			results.close();
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