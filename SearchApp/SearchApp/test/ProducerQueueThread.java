package SearchApp.test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import SearchApp.config.ApplicationProperties;
import SearchApp.client.MqSender;

public class ProducerQueueThread extends Thread {
	private int id;
	
	public  ProducerQueueThread(int id) {
		this.id = id;
	}
	
	@Override
	public void run() {

		MqSender sender = new MqSender();
		
		System.out.println("HOST: " +sender.connnectionFactory.getHost());
		
		JsonElement element = new Gson().toJsonTree(id, int.class);
		JsonObject json = new JsonObject();
		
		String jsonString = "{\"action\":\"createItem\",\"data\":{\"itemName\":\"ipad pro\",\"price\":\"70\",\"desc\":\"item created\",\"categoryID\":\"1\",\"quantity\":\"5\",\"sellerID\":\"1\"}}\r\n";

//		json.add("id", element);
		
//		json.
		

		System.out.println("DATA TO SEND: " + jsonString);
		sender.send(jsonString, null);
		
		return;
	}

}
