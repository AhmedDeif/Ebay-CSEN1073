package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;
import java.util.Map;

public class EditAuctionCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int auctionID, pItemID, pStartPrice;
		Date pStartDate, pEndDate;
		
		auctionID = (int) mapUserData.get("auctionID");
		pItemID = (int) mapUserData.get("pItemID");
		pStartPrice = (int) mapUserData.get("pStartPrice");
		pStartDate = (Date) mapUserData.get("pStartDate");
		pEndDate = (Date) mapUserData.get("pEndDate");
		
		if (pStartDate == null || pEndDate == null)
			return null;
		
		sqlProc = connection.prepareCall("{?=call editAuction(?,?,?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(2, auctionID);
		sqlProc.setInt(3, pItemID);
		sqlProc.setInt(4, pStartPrice);
		sqlProc.setDate(5, pStartDate);
		sqlProc.setDate(5, pEndDate);
		
		sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}

	
}
