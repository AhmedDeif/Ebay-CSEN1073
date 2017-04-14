package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

class DeleteCartCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intID;
                            
        intID    =   Integer.parseInt((String) mapUserData.get( "ID" ));

        if(intID <= 0)
           return null;

        sqlProc = connection.prepareCall("{call deleteCart(?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intID);
        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
        sqlProc.close( );

        return strbufResult;
    }
}