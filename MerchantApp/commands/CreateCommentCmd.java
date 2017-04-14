package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

//// Comments Cmd Nesreen
class CreateCommentCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        String              strComment_text;
        int                 intItemID,
                            intUserID;
                            
        strComment_text    =   (String)mapUserData.get( "comment_text" );
        intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));
        intUserID =   Integer.parseInt((String)mapUserData.get( "userID"));

        if( strComment_text == null || strComment_text.trim( ).length( ) == 0 ||
            intItemID <= 0 || intUserID <= 0 )
           return null;

        sqlProc = connection.prepareCall("{call createComment(?,?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setString(1, strComment_text);
        sqlProc.setInt(2, intItemID);
        sqlProc.setInt(3, intUserID);
        sqlProc.execute();
        StringBuffer sb = new StringBuffer();
        sb.append(sqlProc.getInt(1));
        strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1) , null, sb );
        sqlProc.close( );

        return strbufResult;
    }
}