package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

class DeleteItemCategoryCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intItemID,
                            intCategoryID;
        intItemID =   Integer.parseInt((String) mapUserData.get( "itemID" ));
        intCategoryID =   Integer.parseInt((String) mapUserData.get( "categoryID" ));


        if(intItemID <= 0 || intCategoryID<=0)
           return null;
        sqlProc = connection.prepareCall("{call deleteitemcategory(?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intItemID);
        sqlProc.setInt(2, intCategoryID);
        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
        sqlProc.close( );

        return strbufResult;
    }
}