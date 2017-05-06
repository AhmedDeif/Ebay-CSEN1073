package BiddingApp.commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;
import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Map;


public class CreateAuctionCmd extends Command implements Runnable {

	@Override
	public StringBuffer execute(Connection connection,
			Map<String, Object> mapUserData) throws Exception {
		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		int pUID, pItemID, pStartPrice;
		Date pStartDate, pEndDate;
		
		pUID = Integer.parseInt(mapUserData.get("pUID").toString()) ;
		pItemID = Integer.parseInt(mapUserData.get("pItemID").toString()) ;
		pStartPrice = Integer.parseInt(mapUserData.get("pStartPrice").toString()) ;
		

		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date = sdf1.parse(mapUserData.get("pStartDate").toString());
		pStartDate = new java.sql.Date(date.getTime());
	

		sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		date = sdf1.parse(mapUserData.get("pEndDate").toString());
		pEndDate = new java.sql.Date(date.getTime());

		if (pStartDate == null || pEndDate == null)
			return null;
		
		sqlProc = connection.prepareCall("{call createAuction(?,?,?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setInt(1, pUID);
		sqlProc.setInt(2, pItemID);
		sqlProc.setInt(3, pStartPrice);
		sqlProc.setDate(4, pStartDate);
		sqlProc.setDate(5, pEndDate);
		
		sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}

}
