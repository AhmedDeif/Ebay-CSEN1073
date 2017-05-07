package UserApp.commands;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

public class AddUserSimpleCmd extends Command implements Runnable {

		public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

//			StringBuffer strbufResult;
//			CallableStatement sqlProc;
//			String strEmail, strPassword, strFirstName, strLastName;
//			strEmail = (String) mapUserData.get("email");
//			strPassword = (String) mapUserData.get("password");
//			strFirstName = (String) mapUserData.get("firstName");
//			strLastName = (String) mapUserData.get("lastName");
//			String gender = (String) mapUserData.get("gender");
//			String phone = (String) mapUserData.get("phone");
//
//			if (strEmail == null || strEmail.trim().length() == 0 || strPassword == null
//					|| strPassword.trim().length() == 0 || strFirstName == null || strFirstName.trim().length() == 0
//					|| strLastName == null || strLastName.trim().length() == 0)
//				throw new Exception("Add users parameterss are missing");
//
//			if (!EmailVerifier.verify(strEmail))
//				throw new Exception("Cannot verify email");
//
//			System.out.println("ADD USER SIMPLEEEE");
//			
//			sqlProc = connection.prepareCall("{?=call addUserSimple(?,?,?,?,?,?)}");
//			sqlProc.registerOutParameter(1, Types.INTEGER);
//			sqlProc.setString(2, strEmail);
//			sqlProc.setString(3, strPassword);
//			sqlProc.setString(4, strFirstName);
//			sqlProc.setString(5, strLastName);
//			sqlProc.setString(6, gender);
//			sqlProc.setString(7, phone);
//
//			sqlProc.execute();
//			strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
//			sqlProc.close();
//
//			return strbufResult;
			
			
			
			
			StringBuffer strbufResult;
			CallableStatement sqlProc;
			String firstName, lastName, email, password, gender;

			firstName = (String) mapUserData.get("firstName");
			lastName = (String) mapUserData.get("lastName");
			email = (String) mapUserData.get("email");
			password = (String) mapUserData.get("password");
			
			if (firstName == null || firstName.trim().length() == 0 ||
					lastName == null || lastName.trim().length() == 0 ||
					email == null || email.trim().length() == 0 ||
					password == null || password.trim().length() == 0) {
				StringBuffer errorBuffer = new StringBuffer();
				JsonObject error = new JsonObject();
				error.addProperty("errorMsg", "error");
				errorBuffer.append(error.toString());
				return errorBuffer;
			}
			
			// gender = (String) mapUserData.get("gender");

			// if (firstName == null || strItemName.trim().length() == 0 || dblPrice
			// <= 0 || intQuantity <= 0
			// || intSellerID <= 0)
			// return null;
			System.out.println("ADD USER SIMPLEEEE");
			
			System.out.println("nameeee:  " + firstName);

			sqlProc = connection.prepareCall("{call addUser(?,?,?,?)}");
			sqlProc.registerOutParameter(1, Types.INTEGER);
			sqlProc.setString(1, firstName);
			sqlProc.setString(2, lastName);
			sqlProc.setString(3, email);
			sqlProc.setString(4, password);
			sqlProc.execute();
			StringBuffer sb = new StringBuffer();
			sb.append(sqlProc.getInt(1));

			strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);

		

//			Jedis jedis = new Jedis("localhost");
//			jedis.set("user_id", "" + sqlProc.getInt(1));
			sqlProc.close();
			if (!sb.toString().equals(null)) {
				strbufResult = makeJSONResponseEnvelope(200, null, sb);
//				sqlProc.close();

				return strbufResult;
			} else {
//				sqlProc.close();
				System.out.println("DB returned null!");
				StringBuffer errorBuffer = new StringBuffer();
				JsonObject error = new JsonObject();
				error.addProperty("errorMsg", "error");
				errorBuffer.append(error.toString());
				return errorBuffer;
			}
		}
	}
