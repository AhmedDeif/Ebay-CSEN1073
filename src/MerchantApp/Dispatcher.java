package MerchantApp;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.zaxxer.hikari.HikariDataSource;


//import Dispatcher.Command;

public class Dispatcher {

    protected Hashtable _htblCommands;
    protected ExecutorService _threadPoolCmds;
    protected HikariDataSource _hikariDataSource;
    protected DB db;

    public Dispatcher() {
    }

    abstract class Command {

        protected HikariDataSource _hikariDataSource;
        protected ClientHandle _clientHandle;
        protected ClientRequest _clientRequest;

        protected ArrayList<String> _arrColsToKeep;

        public void init(HikariDataSource hikariDataSource, ClientHandle clientHandle, ClientRequest clientRequest) {
            _hikariDataSource = hikariDataSource;
            _clientRequest = clientRequest;
            _clientHandle = clientHandle;
        }

        public void run() {
            Connection connection = null;

            try {
                Map<String, Object> map;
                StringBuffer strbufResponse;
                connection = _hikariDataSource.getConnection();
                map = _clientRequest.getData();
                strbufResponse = execute(connection, map);

                if (strbufResponse != null)
                    _clientHandle.passResponsetoClient(strbufResponse);
                else
                    _clientHandle.terminateClientRequest();
            } catch (Exception exp) {
                System.err.println(exp.toString());
                _clientHandle.terminateClientRequest();
            } finally {
                closeConnectionQuietly(connection);
            }
        }

        protected void closeConnectionQuietly(Connection connection) {
            try {
                if (connection != null)
                    connection.close();
            } catch (Exception exp) {
                // log this...
                exp.printStackTrace();
            }
        }

        protected StringBuffer makeJSONResponseEnvelope(int nResponse, StringBuffer strbufRequestData,
                StringBuffer strbufResponseData) {
        	
        	  StringBuffer strbufJSON;
              String strStatusMsg;
              String strData = "";
              Map<String, Object> mapInputData;
              String strKey;
        	
        	if(strbufResponseData.equals(null)){
        		
        		nResponse = 400;
        	}
        	else{
        		nResponse = 200;
        	}
        	
          

            strbufJSON = new StringBuffer();
            strbufJSON.append("{");
            strbufJSON.append("\"responseTo\":\"" + _clientRequest.getAction() + "\",");
            if (_clientRequest.getSessionID() != null)
                strbufJSON.append("\"sessionID\":\"" + _clientRequest.getSessionID() + "\",");

            strbufJSON.append("\"StatusID\":\"" + nResponse + "\",");
            strStatusMsg = (String) ResponseCodes.getMessage(Integer.toString(nResponse));
            strbufJSON.append("\"StatusMsg\":\"" + strStatusMsg + "\",");

            if (strbufRequestData != null)
                strbufJSON.append("\"requestData\":{" + strbufRequestData + "},");

            if (strbufResponseData != null) {
                if (strbufResponseData.charAt(0) == '[')
                    strbufJSON.append("\"responseData\":" + strbufResponseData); // if
                                                                                    // it
                                                                                    // is
                                                                                    // a
                                                                                    // list,
                                                                                    // no
                                                                                    // curley
                else
                    strbufJSON.append("\"responseData\":{" + strbufResponseData + "}");
            }
            if (strbufJSON.charAt(strbufJSON.length() - 1) == ',')
                strbufJSON.deleteCharAt(strbufJSON.length() - 1);

            strbufJSON.append("}");
            return strbufJSON;
        }

        protected StringBuffer serializeRequestDatatoJSON(ArrayList arrFieldstoKeep) throws Exception {

            return serializeMaptoJSON(_clientRequest.getData(), arrFieldstoKeep);
        }

        protected StringBuffer serializeResultSettoJSON(ResultSet resultSet, ArrayList arrColstoKeep, int nMaxSize)
                throws Exception {

            StringBuffer strbufJSON;
            int nCount;
            boolean bKeepColumn;

            strbufJSON = new StringBuffer();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            strbufJSON.append("[ ");
            nCount = 0;

            while (resultSet.next() && ((nCount < nMaxSize) || (nMaxSize == 0))) {
                int nColumns = rsmd.getColumnCount();
                strbufJSON.append("{");
                nCount++;
                for (int nIndex = 1; nIndex < nColumns + 1; nIndex++) {
                    String strColumnName = rsmd.getColumnName(nIndex);
                    bKeepColumn = false;
                    if (arrColstoKeep == null)
                        bKeepColumn = true;
                    else if (arrColstoKeep.contains(strColumnName))
                        bKeepColumn = true;

                    if (bKeepColumn) {
                        strbufJSON.append("\"" + strColumnName + "\": ");

                        if (rsmd.getColumnType(nIndex) == java.sql.Types.BIGINT)
                            strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.BOOLEAN)
                            strbufJSON.append("\"" + resultSet.getBoolean(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.BLOB)
                            strbufJSON.append("\"" + resultSet.getBlob(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.DOUBLE)
                            strbufJSON.append("\"" + resultSet.getDouble(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.FLOAT)
                            strbufJSON.append("\"" + resultSet.getFloat(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.INTEGER)
                            strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.NVARCHAR)
                            strbufJSON.append("\"" + resultSet.getNString(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.VARCHAR)
                            strbufJSON.append("\"" + resultSet.getString(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.TINYINT)
                            strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.SMALLINT)
                            strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.DATE)
                            strbufJSON.append("\"" + resultSet.getDate(nIndex) + "\"");
                        else if (rsmd.getColumnType(nIndex) == java.sql.Types.TIMESTAMP)
                            strbufJSON.append("\"" + resultSet.getTimestamp(nIndex) + "\"");
                        else
                            strbufJSON.append("\"" + resultSet.getObject(nIndex) + "\"");

                        strbufJSON.append(",");
                    }
                }
                if (strbufJSON.charAt(strbufJSON.length() - 1) == ',')
                    strbufJSON.setLength(strbufJSON.length() - 1);
                strbufJSON.append("},");
            }

            if (strbufJSON.charAt(strbufJSON.length() - 1) == ',')
                strbufJSON.setLength(strbufJSON.length() - 1);

            strbufJSON.append("]");

            return strbufJSON;
        }

        protected StringBuffer serializeMaptoJSON(Map<String, Object> map, ArrayList arrFieldstoKeep) {

            StringBuffer strbufData;

            strbufData = new StringBuffer();
            if (arrFieldstoKeep == null) {
                for (Map.Entry<String, Object> entry : map.entrySet())
                    strbufData.append("\"" + entry.getKey() + "\":\"" + entry.getValue().toString() + "\",");
            } else {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (arrFieldstoKeep.contains(entry.getKey()))
                        strbufData.append("\"" + entry.getKey() + "\":\"" + entry.getValue().toString() + "\",");
                }
            }

            if (strbufData.charAt(strbufData.length() - 1) == ',')
                strbufData.setLength(strbufData.length() - 1);

            return strbufData;
        }

        public abstract StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception;

    }
    
 
    /////////////////START Nesreen ////////////////////////////
    //// Item Cmds Nesreen
    class CreateItemCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {
            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            String              strItemName,
                                strDecription;
            int                 intQuantity,
            					intCategoryID,
        	 				    nSQLResult,
                                intSellerID,
                                dblPrice;
            
            strItemName   = (String)mapUserData.get( "itemName" );
            strDecription = (String)mapUserData.get( "description" );
            intQuantity =   Integer.parseInt((String) mapUserData.get( "quantity" ));
            intSellerID =   Integer.parseInt((String) mapUserData.get( "sellerID" ));
            intCategoryID =   Integer.parseInt((String) mapUserData.get( "categoryID" ));

            dblPrice   = 	Integer.parseInt((String) mapUserData.get( "price" ));


            if( strItemName == null || strItemName.trim( ).length( ) == 0 ||
                dblPrice <= 0 || intQuantity <= 0 || intSellerID <= 0 )
               return null;

            sqlProc = connection.prepareCall("{call createitem(?,?,?,?,?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setString(1, strItemName);
            sqlProc.setInt(2 , dblPrice);
            sqlProc.setString(3, strDecription);
            sqlProc.setInt(4, intCategoryID);
            sqlProc.setInt(5, intQuantity);
            sqlProc.setInt(6, intSellerID);
            sqlProc.execute();
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt(1) , null,  sb);
            System.out.println("@@@@ " + sqlProc.getInt(1) );
            sqlProc.close( );
            
            // Mongo connection 
            // To connect to mongodb server
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
            // Now connect to your databases
            db = mongoClient.getDB("EbaySearch");
            System.out.println("Connect to database successfully");
            
            // insert item in mongoDb 
            DBCollection coll = db.getCollection("items");
            System.out.println("Collection mycol selected successfully");
   			
            BasicDBObject doc = new BasicDBObject("itemName", strItemName)
                   .append("price", dblPrice)
                   .append("description",strDecription )
                   .append("quantity", intQuantity)
                   .append("seller", new BasicDBObject("id", intSellerID));
           
            coll.insert(doc);
            
            return strbufResult;
        }
    }
    class EditItemCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            String              strItemName,
                                strDecription;
            int                 intQuantity,
                                intItemID,
                                intSellerID,
                                dblPrice;
                                
            strItemName    =   (String)mapUserData.get( "itemName" );
            strDecription =   (String)mapUserData.get( "description" );
            intQuantity =   Integer.parseInt((String)mapUserData.get( "quantity" ));
            intSellerID =   Integer.parseInt((String)mapUserData.get( "sellerID"));
            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));
            dblPrice   = Integer.parseInt((String)mapUserData.get( "price"));

            if( strItemName == null || strItemName.trim( ).length( ) == 0 ||
                dblPrice <= 0 || intQuantity <= 0 || intSellerID <= 0 ||
                intItemID <= 0)
               return null;

            

            sqlProc = connection.prepareCall("{call editItem(?,?,?,?,?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intItemID);
            sqlProc.setString(2, strItemName);
            sqlProc.setInt(3, dblPrice);
            sqlProc.setString(4, strDecription);
            sqlProc.setInt(5, intQuantity);
            sqlProc.setInt(6, intSellerID);

            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class DeleteItemCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intItemID;

            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID" ));

            if(intItemID <= 0)
               return null;

            

            sqlProc = connection.prepareCall("{call deleteItem(?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intItemID);
            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class ViewItemCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int					nSQLResult;

            connection.setAutoCommit(false);
            sqlProc = connection.prepareCall("{?=call viewItem()}");
            sqlProc.registerOutParameter(1, Types.OTHER );
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
        	   sb.append(metaData.getColumnName(count)+" : " + results.getString(count));

            }
            strbufResult = makeJSONResponseEnvelope( 1 , null, sb );
            
            results.close();
            sqlProc.close();
            return strbufResult;
        }
    }
    class FindItemCmd extends Command implements Runnable {

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
//            ResultSet results = sqlProc.executeQuery();

//            nSQLResult = sqlProc.getInt(1);
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
//            if( nSQLResult >= 0 ){
//                // Cache.addSession( strSessionID, strEmail );
//                System.err.println(" view items" );
//                Map<String, Object> mapResult = new HashMap<String, Object>( );
//                
//                mapResult.put( "item", Integer.toString( nSQLResult));
//                strbufResponseJSON  =   serializeMaptoJSON( mapResult, null );
//                strbufResult = makeJSONResponseEnvelope( 0, null, strbufResponseJSON  );
//            }
//            else
//                strbufResult = makeJSONResponseEnvelope( nSQLResult , null, null );

            return strbufResult;
        }
    }
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
    class EditCommentCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            String              strComment_text;
            int                 intItemID,
                                intCommentID,
                                intUserID;
                                
            strComment_text    =   (String)mapUserData.get( "comment_text" );
            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));
            intUserID =   Integer.parseInt((String)mapUserData.get( "userID" ));
            intCommentID =   Integer.parseInt((String)mapUserData.get( "commentID"));


              if( strComment_text == null || strComment_text.trim( ).length( ) == 0 ||
                intItemID <= 0 || intUserID <= 0 || intCommentID <= 0 )
                     return null;

            

            sqlProc = connection.prepareCall("{call editComment(?,?,?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setString(1, strComment_text);
            sqlProc.setInt(2, intItemID);
            sqlProc.setInt(3, intUserID);
            sqlProc.setInt(4, intCommentID);

            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class ViewCommentCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int					nSQLResult;

            connection.setAutoCommit(false);
            sqlProc = connection.prepareCall("{?=call viewComment()}");
            sqlProc.registerOutParameter(1, Types.OTHER );
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
        	   sb.append(metaData.getColumnName(count)+" : " + results.getString(count));

            }
            strbufResult = makeJSONResponseEnvelope( 1 , null, sb );
            
            results.close();
            sqlProc.close();
            return strbufResult;
        }
    }
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
    //// Rating Cmd Nesreen
    class CreateUserRatingCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intItemID,
                                intUserID,
                                intRating;
                                
            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));
            intUserID =   Integer.parseInt((String)mapUserData.get( "userID" ));
            intRating =   Integer.parseInt((String)mapUserData.get( "rating"));

            if( intItemID <= 0 || intUserID <= 0 || intRating <= 0 )
               return null;

            sqlProc = connection.prepareCall("{call createUserRating(?,?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intUserID);
            sqlProc.setInt(2, intItemID);
            sqlProc.setInt(3, intRating);
            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope(sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class EditUserRatingCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intItemID,
                                intUserID,
                                intRating;
                                
            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));
            intUserID =   Integer.parseInt((String)mapUserData.get( "userID" ));
            intRating =   Integer.parseInt((String)mapUserData.get( "rating" ));

            if(intItemID <= 0 || intUserID <= 0 || intRating <= 0)
               return null;

            

            sqlProc = connection.prepareCall("{call editUserRating(?,?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intUserID);
            sqlProc.setInt(2, intItemID);
            sqlProc.setInt(3, intRating);
            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class DeleteUserRatingCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intItemID,
                                intUserID;

            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID" ));
            intUserID =   Integer.parseInt((String)mapUserData.get( "userID" ));


            if(intItemID <= 0 || intUserID <= 0)
               return null;

            sqlProc = connection.prepareCall("{call deleteUserRating(?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intItemID);
            sqlProc.setInt(2, intUserID);
            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class ViewItemUserRatingCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int                 intItemID,nSQLResult;

            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID" ));

            if(intItemID <= 0)
                return null;
            connection.setAutoCommit(false);
            sqlProc = connection.prepareCall("{call viewItemUserRating(?)}");
            sqlProc.registerOutParameter(1, Types.OTHER );
            sqlProc.setInt(1, intItemID);
            sqlProc.execute( );
            ResultSet results = (ResultSet) sqlProc.getObject(1);
            ResultSetMetaData metaData = results.getMetaData();
            int count = metaData.getColumnCount();
            StringBuffer sb  = new StringBuffer();
            while(results.next()){
            	
	           for (int i = 1; i <=count-1; i++) {
	        	   sb.append(metaData.getColumnName(i)+" : " + results.getString(i)+",");

	        	   System.out.println(results.getString(i));
	           }
        	   sb.append(metaData.getColumnName(count)+" : " + results.getString(count));

            }
            strbufResult = makeJSONResponseEnvelope( 1 , null, sb );
            
            results.close();
            sqlProc.close();
            return strbufResult;
        }
    }
    class FindItemUserRatingCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int                 intItemID,nSQLResult,
                                intUserID;
        	System.out.println("ana hena34343444334 ");

            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));
            intUserID =   Integer.parseInt((String)mapUserData.get( "userID"));

            if(intItemID <= 0 || intUserID <= 0 )
                return null;
            connection.setAutoCommit(false);
            	System.out.println("ana hena ");
            sqlProc = connection.prepareCall("{call findItemUserRating(?,?)}");
            sqlProc.registerOutParameter(1, Types.OTHER );
            sqlProc.setInt(1, intItemID);
            sqlProc.setInt(2, intUserID);
            sqlProc.execute( );
            System.out.println("ana hena 222 ");

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

            return strbufResult;
        }
    }
    class CalculateRatingCmd extends Command implements Runnable{

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intItemID;
                                                              
            intItemID =   Integer.parseInt((String)mapUserData.get( "itemID"));
  
            if(intItemID <= 0)
               return null;

            sqlProc = connection.prepareCall("{call calculateRating(?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intItemID);
            sqlProc.execute();
            System.out.println("avg: " + sqlProc.getInt(1));
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt(1) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    /////////////////END Nesreen ////////////////////////////   
      
        /////////RANAAAAAA//////////////////////
    //// Category Cmd
    class CreateCategoryCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            String              strCategoryName;
                                
            strCategoryName    =   (String)mapUserData.get( "categoryName" );

            if( strCategoryName == null || strCategoryName.trim( ).length( ) == 0)
               return null;

            sqlProc = connection.prepareCall("{call createCategory(?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setString(1, strCategoryName);
            sqlProc.execute();
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            
//          insert item in mongoDb 
            System.out.println("######");
            System.out.println(strCategoryName);

//            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//			
//            // Now connect to your databases
//             db = mongoClient.getDB("EbaySearch");
             if(!db.collectionExists(strCategoryName)){
            	 DBCollection coll = db.createCollection(strCategoryName, new BasicDBObject());

             }
             
          
             System.out.println("Done mongo db");
            
            return strbufResult;
        }
    }

    class EditCategoryCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            String              strCategoryName;
            int                 intID;
                                
            strCategoryName    =   (String)mapUserData.get( "categoryName" );
            intID= Integer.parseInt((String) mapUserData.get( "id"));
//            System.out.println("category name " + strCategoryName);
//            System.out.println("id " + intID);

              if( strCategoryName == null || strCategoryName.trim( ).length( ) == 0 || intID <= 0 )
                     return null;

            sqlProc = connection.prepareCall("{call editCategory(?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setString(2, strCategoryName);
            sqlProc.setInt(1, intID);

            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class FindCategoryCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int                 intID,nSQLResult;

            intID= Integer.parseInt((String) mapUserData.get( "id"));

            if(intID <= 0 )
                return null;
            
            connection.setAutoCommit(false);
            sqlProc = connection.prepareCall("{call findCategory(?)}");
            sqlProc.registerOutParameter(1,  Types.OTHER);
            sqlProc.setInt(1, intID);
            sqlProc.execute( );

            ResultSet results = (ResultSet) sqlProc.getObject(1);
            ResultSetMetaData metaData = results.getMetaData();
            int count = metaData.getColumnCount();
            StringBuffer sb= new StringBuffer();
            while(results.next()){
        
                for (int i = 1; i <=count-1; i++) {
 	        	   sb.append(metaData.getColumnName(i)+" : " + results.getString(i)+",");

                    System.out.println(results.getString(i));
                 
                }
	        	   sb.append(metaData.getColumnName(count)+" : " + results.getString(count)+",");

                System.out.println(results.getRow());
                System.out.println("Count " + count);
            }

            strbufResult = makeJSONResponseEnvelope( 1 , null, sb );
            
            results.close();
            sqlProc.close();
            
            
            
            return strbufResult;
        }
    }
    class ViewCategoryCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int 					nSQLResult;
            
            connection.setAutoCommit(false);
            sqlProc = connection.prepareCall("{?=call viewCategory()}");
            sqlProc.registerOutParameter(1, Types.OTHER );
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

            }
            strbufResult = makeJSONResponseEnvelope( 1 , null, sb );
            
            results.close();
            sqlProc.close();
            return strbufResult;
     
        }
    }
    class DeleteCategoryCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intID;

//            intID =   (int)mapUserData.get( "ID" );
            intID = Integer.parseInt((String) mapUserData.get( "id" ));


            if(intID <= 0 )
               return null;

            sqlProc = connection.prepareCall("{?=call deleteCategory(?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(2, intID);
            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    //// Item Category Cmd
    class CreateItemCategoryCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intItemID,
                                intCategoryID;

            intItemID = Integer.parseInt((String) mapUserData.get( "itemid" ));
//            System.out.println("---> " + intItemID);
            intCategoryID  = Integer.parseInt((String) mapUserData.get( "categoryid" ));
//            System.out.println("--->> " + intCategoryID);

            if( intItemID <= 0  || 
                intCategoryID <= 0 )
               return null;

            sqlProc = connection.prepareCall("{?=call createItemCategory(?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(2, intItemID);
            sqlProc.setInt(3, intCategoryID);
            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class FindItemCategoryCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int                 intItemID,intCategoryID,nSQLResult;

            intItemID= Integer.parseInt((String) mapUserData.get( "itemid"));
            intCategoryID= Integer.parseInt((String) mapUserData.get( "categoryid"));


            if(intItemID <= 0)
                return null;   
            
            connection.setAutoCommit(false);
            sqlProc = connection.prepareCall("{?=call findItemCategory(?)}");
            sqlProc.registerOutParameter(1,  Types.OTHER);
            sqlProc.setInt(1, intItemID);
            sqlProc.setInt(2, intCategoryID);
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
                System.out.println("Count "+ count);
            }

            strbufResult = makeJSONResponseEnvelope( 1 , null, sb );
            
            results.close();
            sqlProc.close();

            return strbufResult;
        }
    }
    //////////////RAAAANAAAAAA///////////////

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
    //// Cart Cmd 
    class CreateCartCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intUserID;
                                
            intUserID    =   Integer.parseInt((String) mapUserData.get( "userID") );

            if(intUserID <= 0)
               return null;

            sqlProc = connection.prepareCall("{call createCart(?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intUserID);
            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
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
    //// Cart Item Cmd
    class AddItemToCartCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intCartID,
                                intItemID,
                                intQuantity;
                                
            intCartID    =   Integer.parseInt((String) mapUserData.get( "cartID" ));
            intItemID    =   Integer.parseInt((String) mapUserData.get( "itemID" ));
            intQuantity    =   Integer.parseInt((String) mapUserData.get( "quantity" ));

            if(intCartID <= 0 || intItemID <= 0 || intQuantity <= 0 ) 
               return null;

            sqlProc = connection.prepareCall("{call AddItemToCart(?,?,?)}");
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
    class DeleteItemInCartCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult;
            CallableStatement   sqlProc;
            int                 intCartID,
                                intItemID;
                                
            intCartID    =   Integer.parseInt((String) mapUserData.get( "cartID" ));
            intItemID    =   Integer.parseInt((String) mapUserData.get( "itemID" ));

            if(intCartID <= 0 ||
                intItemID <= 0 )
               return null;

            sqlProc = connection.prepareCall("{call deleteItemInCart(?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER );
            sqlProc.setInt(1, intCartID);
            sqlProc.setInt(2, intItemID);

            sqlProc.execute( );
            StringBuffer sb = new StringBuffer();
            sb.append(sqlProc.getInt(1));
            strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, sb );
            sqlProc.close( );

            return strbufResult;
        }
    }
    class ViewItemsInCartCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

            StringBuffer        strbufResult,strbufResponseJSON;
            CallableStatement   sqlProc;
            int                 intCartID,nSQLResult;
            
            intCartID    =   Integer.parseInt((String) mapUserData.get( "cartID" ));
            
            connection.setAutoCommit(false);
            sqlProc = connection.prepareCall("{call viewItemsInCart(?)}");
            sqlProc.registerOutParameter(1, Types.OTHER );
            sqlProc.setInt(1, intCartID);
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

            }
            strbufResult = makeJSONResponseEnvelope( 1  , null, sb );
            results.close();
            sqlProc.close( );

            return strbufResult;
        }
    }

    
    
    class AddUserSimpleCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

            StringBuffer strbufResult;
            CallableStatement sqlProc;
            String strEmail, strPassword, strFirstName, strLastName;
            strEmail = (String) mapUserData.get("email");
            strPassword = (String) mapUserData.get("password");
            strFirstName = (String) mapUserData.get("firstName");
            strLastName = (String) mapUserData.get("lastName");

            if (strEmail == null || strEmail.trim().length() == 0 || strPassword == null
                    || strPassword.trim().length() == 0 || strFirstName == null || strFirstName.trim().length() == 0
                    || strLastName == null || strLastName.trim().length() == 0)
                throw new Exception("Add users parameterss are missing");

            if (!EmailVerifier.verify(strEmail))
                throw new Exception("Cannot verify email");

            sqlProc = connection.prepareCall("{?=call addUserSimple(?,?,?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER);
            sqlProc.setString(2, strEmail);
            sqlProc.setString(3, strPassword);
            sqlProc.setString(4, strFirstName);
            sqlProc.setString(5, strLastName);

            sqlProc.execute();
            strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
            sqlProc.close();

            return strbufResult;
        }
    }

    class AttemptLoginCmd extends Command implements Runnable {

        public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

            CallableStatement sqlProc;
            StringBuffer strbufResult = null, strbufResponseJSON;
            String strSessionID, strEmail, strPassword, strFirstName, strClientIP;
            int nSQLResult;

            strEmail = ((String) mapUserData.get("email"));
            strPassword = ((String) mapUserData.get("password"));

            if (strEmail == null || strEmail.trim().length() == 0 || strPassword == null
                    || strPassword.trim().length() == 0)
                return null;

            if (!EmailVerifier.verify(strEmail))
                return null;

            strClientIP = _clientHandle.getClientIP();
            strSessionID = UUID.randomUUID().toString();

            sqlProc = connection.prepareCall("{?=call attemptLogin(?,?,?,?)}");
            sqlProc.registerOutParameter(1, Types.INTEGER);
            sqlProc.setString(2, strEmail);
            sqlProc.setString(3, strPassword);
            sqlProc.setString(4, strSessionID);
            sqlProc.setString(5, strClientIP);
            sqlProc.execute();
            nSQLResult = sqlProc.getInt(1);
            sqlProc.close();
            if (nSQLResult >= 0) {
                Cache.addSession(strSessionID, strEmail);
                System.err.println(" adding following session to Cache " + strSessionID);
                Map<String, Object> mapResult = new HashMap<String, Object>();
                mapResult.put("userID", Integer.toString(nSQLResult));
                mapResult.put("sessionID", strSessionID);
                sqlProc = connection.prepareCall("{?=call getUserFirstName(?)}");
                sqlProc.registerOutParameter(1, Types.VARCHAR);
                sqlProc.setInt(2, nSQLResult);
                sqlProc.execute();
                strFirstName = sqlProc.getString(1);
                sqlProc.close();
                mapResult.put("firstName", strFirstName);
                strbufResponseJSON = serializeMaptoJSON(mapResult, null);
                strbufResult = makeJSONResponseEnvelope(0, null, strbufResponseJSON);
            } else
                strbufResult = makeJSONResponseEnvelope(nSQLResult, null, null);

            return strbufResult;
        }
    }

    protected void dispatchRequest(ClientHandle clientHandle, ClientRequest clientRequest) throws Exception {

        Command cmd;
        String strAction;
        strAction = clientRequest.getAction();

        Class<?> innerClass = (Class<?>) _htblCommands.get(strAction);
        Class<?> enclosingClass = Class.forName("Dispatcher");
        Object enclosingInstance = enclosingClass.newInstance();
        Constructor<?> ctor = innerClass.getDeclaredConstructor(enclosingClass);
        cmd = (Command) ctor.newInstance(enclosingInstance);
        cmd.init(_hikariDataSource, clientHandle, clientRequest);
        _threadPoolCmds.execute((Runnable) cmd);
    }

    protected void loadHikari(String strAddress, int nPort, String strDBName, String strUserName, String strPassword) {

        _hikariDataSource = new HikariDataSource();
        _hikariDataSource.setJdbcUrl("jdbc:postgresql://" + strAddress + ":" + nPort + "/" + strDBName);
        _hikariDataSource.setUsername(strUserName);
        _hikariDataSource.setPassword(strPassword);
        
//        _hikariDataSource.setJdbcUrl("mongodb://localhost:27017/EbaySearch");
  
    }

    protected void loadCommands() throws Exception {
        _htblCommands = new Hashtable();
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("commands.properties");
        prop.load(in);
        in.close();
        Enumeration enumKeys = prop.propertyNames();
        String strActionName, strClassName;

        while (enumKeys.hasMoreElements()) {
            strActionName = (String) enumKeys.nextElement();
            strClassName = (String) prop.get(strActionName);
            Class<?> innerClass = Class.forName("Dispatcher$" + strClassName);
            _htblCommands.put(strActionName, innerClass);
        }
    }

    protected void loadThreadPool() {
        _threadPoolCmds = Executors.newFixedThreadPool(20);
    }
    
    public void mongodbConnection(){
    	 try{
    			
             // To connect to mongodb server
             MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
             // Now connect to your databases
             db = mongoClient.getDB("EbaySearch");
             System.out.println("Connect to database successfully");

//             DBCollection coll = db.createCollection("fashion" , new BasicDBObject());    			
          }catch(Exception e){
             System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          }
    }

    public void init() throws Exception {
        loadHikari("localhost", 5432, "ebay", "postgres", "41319");
        //mongodb Connection
//        mongodbConnection();
        loadThreadPool();
        loadCommands();
        
    }
}