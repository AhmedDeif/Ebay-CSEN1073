package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;


public class ReadAuctionCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int pUID, pID;
		
		pUID = Integer.parseInt(mapUserData.get("pUID").toString()) ;
		pID = Integer.parseInt(mapUserData.get("pID").toString()) ;
		
		sqlProc = connection.prepareCall("{call getAuctionForUser(?,?)}");
		sqlProc.registerOutParameter(1, Types.REF);
		sqlProc.setInt(1, pUID);
		sqlProc.setInt(2, pID);
		
		sqlProc.execute();
		ResultSet results = (ResultSet) sqlProc.getObject(1);
        ResultSetMetaData metaData = results.getMetaData();
        int count = metaData.getColumnCount();
        StringBuffer sb = new StringBuffer();

        
        while(results.next()){
    
           for (int i = 1; i <=count-1; i++) {
        	   sb.append(metaData.getColumnName(i)+" : " + results.getString(i)+",");
        	   System.out.println(results.getString(i));
			
           }
    	   sb.append(metaData.getColumnName(count)+" : " + results.getString(count));

           System.out.println(results.getRow());
           System.out.println("Count = " + count);
        }

        strbufResult = makeJSONResponseEnvelope( 1 , null, sb );
        
        results.close();
//		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
//		sqlProc.close();
		
		sb.append(sqlProc.getInt(1));
		
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
