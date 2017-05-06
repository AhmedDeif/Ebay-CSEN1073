package MerchantApp.commands;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zaxxer.hikari.HikariDataSource;

import MerchantApp.server.ClientHandle;
import MerchantApp.server.ClientRequest;
import MerchantApp.server.ResponseCodes;

public abstract class Command {

		protected HikariDataSource _hikariDataSource;
		protected ClientHandle _clientHandle;
		protected ClientRequest _clientRequest;

		protected ArrayList<String> _arrColsToKeep;

		public void init(HikariDataSource hikariDataSource, ClientHandle clientHandle, ClientRequest clientRequest) {
			_hikariDataSource = hikariDataSource;
			_clientRequest = clientRequest;
			_clientHandle = clientHandle;
			System.out.println(this.getClass().getName());
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
				exp.printStackTrace();
				StringBuffer strbufError = new StringBuffer();
				String str = "Internal Server Error";
				strbufError.append(str);
				strbufError = makeJSONResponseEnvelope(500, null, strbufError);
				_clientHandle.passResponsetoClient(strbufError);
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
			strStatusMsg = (String) ResponseCodes.getMessage(Integer.toString(nResponse));

			Map<String, Object> propertiesMap = _clientRequest.getProperties();
			Gson gson = new Gson();
			
			JsonObject responseJson = new JsonObject();
			
			JsonElement propertiesJson = gson.toJsonTree(propertiesMap);
			
			responseJson.addProperty("responseTo", _clientRequest.getAction() );
			responseJson.addProperty("statusID", nResponse );
			responseJson.addProperty("statusMsg", strStatusMsg);
			
			
			if (_clientRequest.getSessionID() != null)
				responseJson.addProperty("sessionID", _clientRequest.getSessionID());
			
			
			if (strbufRequestData != null)
				responseJson.addProperty("requestData", strbufRequestData.toString());
			
			if (strbufResponseData != null) {
				JsonParser parser = new JsonParser();
				JsonElement strbufResponseJson = parser.parse(strbufResponseData.toString());
				responseJson.add("responseData", strbufResponseJson);
			}
			
			
			
			responseJson.add("properties", propertiesJson);
			
			StringBuffer resBuf = new StringBuffer(gson.toJson(responseJson));
			return resBuf;
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
