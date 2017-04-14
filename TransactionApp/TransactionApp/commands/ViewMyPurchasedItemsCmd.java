package TransactionApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;


public class ViewMyPurchasedItemsCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int userID;
		
		userID = (int) mapUserData.get("userID");
		
		
		sqlProc = connection.prepareCall("{?=call viewMyPurchasedItems(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(2, userID);

		
		sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}

}
