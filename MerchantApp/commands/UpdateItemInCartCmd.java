package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

class UpdateItemInCartCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        int                 intCartID,
                            intItemID,
                            intQuantity;
        intCartID    =   Integer.parseInt((String) mapUserData.get( "cartID" ));
        intItemID    =   Integer.parseInt((String) mapUserData.get( "itemID" ));
        intQuantity    =   Integer.parseInt((String) mapUserData.get( "quantity" ));

        if(intCartID <= 0 || 
            intItemID <= 0 ||
            intQuantity <= 0)
           return null;

        sqlProc = connection.prepareCall("{call updateItemInCart(?,?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(1, intCartID);
        sqlProc.setInt(2, intItemID);
        sqlProc.setInt(3, intQuantity);

        sqlProc.execute( );
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
        sqlProc.close( );

        return strbufResult;
    }
}