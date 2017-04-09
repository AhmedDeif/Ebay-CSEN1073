package client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * RabbitMQ Receiver based on RabbitMQ java client API
 */
public class MqReceiver {
	private static final Logger log = LoggerFactory.getLogger(MqReceiver.class);
	private ConnectionFactory connnectionFactory;
	private String exchangeName = "NettyMqServerListenerExchange";
	private String queueName = "MqListenerQueue";
	private String routeKey = "MqListenerQueue";
	private String queueTag = "MqTag";

	private Thread listenThread;
	private ClientHandler clientHandler;
	ExecutorService executors = Executors.newFixedThreadPool(10);
	
	
	public MqReceiver(ClientHandler clientHandler) {
		connnectionFactory = new ConnectionFactory();
		connnectionFactory.setHost("localhost");
		connnectionFactory.setUsername("guest");
		connnectionFactory.setPassword("guest");
		connnectionFactory.setPort(5672);
		connnectionFactory.setVirtualHost("/");
		this.clientHandler = clientHandler;
	}

	public void start() {
		try {
			Connection connection = connnectionFactory.newConnection();
			MqRecieverThread thread = new MqRecieverThread(connection, exchangeName, queueName, routeKey, queueTag, clientHandler);
			executors.execute(thread);
			
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
