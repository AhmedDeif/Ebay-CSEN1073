package UserApp.client;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import UserApp.client.MqClientHandler;
import UserApp.client.MqReceiver;
import UserApp.client.MqRecieverThread;
import UserApp.config.ApplicationProperties;

/**
 * RabbitMQ Receiver based on RabbitMQ java client API
 */
public class MqReceiver {
	private static final Logger log = LoggerFactory.getLogger(MqReceiver.class);
	private ConnectionFactory connnectionFactory;

	private String mqRequestHost;
	private int mqRequestPort;
	private String mqRequestUser;
	private String mqRequestPassword;

	private String exchangeName;
	private String queueName;
	private String routeKey;
	private String queueTag;

	private Thread listenThread;
	private MqClientHandler clientHandler;
	ExecutorService executors = Executors.newFixedThreadPool(10);


	public MqReceiver(MqClientHandler clientHandler) {
		connnectionFactory = new ConnectionFactory();
		connnectionFactory.setHost(ApplicationProperties.mqRequestHost);
		connnectionFactory.setUsername(ApplicationProperties.mqRequestUser);
		connnectionFactory.setPassword(ApplicationProperties.mqRequestPassword);
		connnectionFactory.setPort(ApplicationProperties.mqResponsePort);
		connnectionFactory.setRequestedHeartbeat(10);
		connnectionFactory.setVirtualHost("/");
		
		this.clientHandler = clientHandler;

		exchangeName = ApplicationProperties.mqRequestExchangeName;
		queueName =  ApplicationProperties.mqRequestQueueName;
		routeKey  =  ApplicationProperties.mqRequestRouteKey;
		queueTag  = ApplicationProperties.mqRequestQueueTag; 

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
