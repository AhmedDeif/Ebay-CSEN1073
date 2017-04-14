package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;


public class RetrieveWinningBidCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int itemID;
		
		itemID = (int) mapUserData.get("itemID");
		
		
		sqlProc = connection.prepareCall("{?=call retrieveWinningBid(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(2, itemID);

		
		sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}

}
