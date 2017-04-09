package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;



///////////////// START Nesreen ////////////////////////////
//// Item Cmds Nesreen
public class CreateItemCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strItemName, strDecription;
		int intQuantity, nSQLResult, intSellerID, dblPrice;

		strItemName = (String) mapUserData.get("itemName");
		strDecription = (String) mapUserData.get("description");
		intQuantity = Integer.parseInt((String) mapUserData.get("quantity"));
		intSellerID = Integer.parseInt((String) mapUserData.get("sellerID"));
		dblPrice = Integer.parseInt((String) mapUserData.get("price"));

		if (strItemName == null || strItemName.trim().length() == 0 || dblPrice <= 0 || intQuantity <= 0
				|| intSellerID <= 0)
			return null;

		sqlProc = connection.prepareCall("{call createitem(?,?,?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(1, strItemName);
		sqlProc.setInt(2, dblPrice);
		sqlProc.setString(3, strDecription);
		sqlProc.setInt(4, intQuantity);
		sqlProc.setInt(5, intSellerID);
		sqlProc.execute();
		StringBuffer sb = new StringBuffer();
		sb.append(sqlProc.getInt(1));
		strbufResult = makeJSONResponseEnvelope(sqlProc.getInt(1), null, sb);
		System.out.println("@@@@ " + sqlProc.getInt(1));

		sqlProc.close();
		return strbufResult;
	}
}
