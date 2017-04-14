package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

public class FindItemCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult,strbufResponseJSON;
        CallableStatement   sqlProc;
        int                 intItemID,
        						nSQLResult;

        intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));

        if(intItemID <= 0 )
            return null;
        connection.setAutoCommit(false);
        sqlProc = connection.prepareCall("{call findItem(?)}");
        sqlProc.registerOutParameter(1,  Types.OTHER);
        sqlProc.setInt(1, intItemID);
        sqlProc.execute( );
//        ResultSet results = sqlProc.executeQuery();

//        nSQLResult = sqlProc.getInt(1);
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
        sqlProc.close();
//        if( nSQLResult >= 0 ){
//            // Cache.addSession( strSessionID, strEmail );
//            System.err.println(" view items" );
//            Map<String, Object> mapResult = new HashMap<String, Object>( );
//            
//            mapResult.put( "item", Integer.toString( nSQLResult));
//            strbufResponseJSON  =   serializeMaptoJSON( mapResult, null );
//            strbufResult = makeJSONResponseEnvelope( 0, null, strbufResponseJSON  );
//        }
//        else
//            strbufResult = makeJSONResponseEnvelope( nSQLResult , null, null );

        return strbufResult;
    }
}