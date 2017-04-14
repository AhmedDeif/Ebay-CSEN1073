package SearchApp.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import  SearchApp.config.ApplicationProperties;

/**
 * RabbitMQ Sender 
 */
public class MqSender {

	private ConnectionFactory connnectionFactory;

	private String mqSenderHost;
	private int mqSenderPort;
	private String mqSenderUser;
	private String mqSenderPassword;
	
	private String exchangeName;
	private String queueName;
	private String routeKey;
	private String queueTag;

	public MqSender() {
		
		
		connnectionFactory = new ConnectionFactory();
		connnectionFactory.setHost(ApplicationProperties.mqResponseHost);
		connnectionFactory.setUsername(ApplicationProperties.mqResponseUser);
		connnectionFactory.setPassword(ApplicationProperties.mqResponsePassword);
		connnectionFactory.setPort(ApplicationProperties.mqResponsePort);
		connnectionFactory.setVirtualHost("/");
		
		exchangeName = ApplicationProperties.mqResponseExchangeName;
		queueName = ApplicationProperties.mqResponseQueueName;
		routeKey = ApplicationProperties.mqResponseRouteKey;
		queueTag = ApplicationProperties.mqResponseQueueTag;
		
		
	}
	
	public void send(String data) {
		try {
			Connection connection;
			connection = connnectionFactory.newConnection();
			final Channel channel = connection.createChannel();
			channel.exchangeDeclare(exchangeName, "direct", true, false, null);
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchangeName, routeKey);
			
			channel.basicPublish(exchangeName, routeKey, null , data.getBytes());
			
			channel.close();
			connection.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
