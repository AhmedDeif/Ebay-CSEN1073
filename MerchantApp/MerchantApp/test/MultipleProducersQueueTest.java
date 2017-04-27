package MerchantApp.test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import MerchantApp.config.ApplicationProperties;
import MerchantApp.test.ProducerQueueThread;

public class MultipleProducersQueueTest {

	public static void main(String [] args) {
		
		ExecutorService clients = Executors.newFixedThreadPool(200);
		ApplicationProperties.readConfiguration("test.properties");

		
		for (int id = 1; id < 10; id++) {
			clients.execute(new ProducerQueueThread(id));	
		}
		
	}
}
