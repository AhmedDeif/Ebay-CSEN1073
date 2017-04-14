package SearchApp.test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import MerchantApp.client.MqSender;

public class ProducerQueueThread extends Thread {
	private int id;
	
	public  ProducerQueueThread(int id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		MqSender sender = new MqSender();
		
		JsonElement element = new Gson().toJsonTree(id, int.class);
		JsonObject json = new JsonObject();
		
		json.add("id", element);
		
		
		System.out.println("DATA TO SEND: " + json.toString());
		sender.send(json.toString());
		
		return;
	}

}
