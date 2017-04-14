package MerchantApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;

class FindCartCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult,strbufResponseJSON;
        CallableStatement   sqlProc;
        int                 intID,nSQLResult = -1;
                            
        intID    =   Integer.parseInt((String) mapUserData.get( "ID" ));

        if(intID <= 0 )
           return null;
        connection.setAutoCommit(false);
        sqlProc = connection.prepareCall("{call findCart(?)}");
        sqlProc.registerOutParameter(1, Types.OTHER );
        sqlProc.setInt(1, intID);
        sqlProc.execute( );
        ResultSet results = (ResultSet) sqlProc.getObject(1);
        ResultSetMetaData metaData = results.getMetaData();
        int count = metaData.getColumnCount();
        StringBuffer sb = new StringBuffer();
        while(results.next()){
    
            for (int i = 1; i <=count-1; i++) {
	        	   sb.append(metaData.getColumnName(i)+" : " + results.getString(i)+",");

                System.out.println(results.getString(i));
             
            }
        	   sb.append(metaData.getColumnName(count)+" : " + results.getString(count)+",");

            System.out.println(results.getRow());
            System.out.println("Count = " + count);
        }
        strbufResult = makeJSONResponseEnvelope(  1  , null, sb );
        results.close();
        sqlProc.close( );

        return strbufResult;
    }
}