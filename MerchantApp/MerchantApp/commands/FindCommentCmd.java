package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

class FindCommentCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult,strbufResponseJSON;
    
        CallableStatement   sqlProc;
        int                 intCommentID,
        	                     nSQLResult;

        intCommentID =   Integer.parseInt((String)mapUserData.get("commentID"));

        if(intCommentID <= 0 )
            return null;
        
        connection.setAutoCommit(false);
        sqlProc = connection.prepareCall("{call findComment(?)}");
        sqlProc.registerOutParameter(1, Types.OTHER );
        sqlProc.setInt(1, intCommentID);
        sqlProc.execute( );
        ResultSet results = (ResultSet) sqlProc.getObject(1);
        ResultSetMetaData metaData = results.getMetaData();
        StringBuffer sb = new StringBuffer();
        int count = metaData.getColumnCount();
        
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
        sqlProc.close();

        return strbufResult;
    }
}