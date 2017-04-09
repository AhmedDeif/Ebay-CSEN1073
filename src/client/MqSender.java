package client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * RabbitMQ Sender based on Spring AMQP
 */
public class MqSender {

	private ConnectionFactory connnectionFactory;

	private String exchangeName = "NettyMqServerListenerExchange";
	private String queueName = "MqListenerQueue";
	private String routeKey = "MqListenerQueue";
	private String queueTag = "MqTag";


	public MqSender(String exchangeName, String queueName, String routeKey, String queueTag) {
//		String producerConfigPath
		//		@SuppressWarnings("resource")
//		ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
//				"classpath:" + producerConfigPath);
//			
//
//		rabbitTemplate = (RabbitTemplate) applicationContext
//				.getBean("messageSender");
//		

		connnectionFactory = new ConnectionFactory();
		connnectionFactory.setHost("localhost");
		connnectionFactory.setUsername("guest");
		connnectionFactory.setPassword("guest");
		connnectionFactory.setPort(5672);
		connnectionFactory.setVirtualHost("/");
		
		this.exchangeName = exchangeName;
		this.queueName = queueName;
		this.routeKey = routeKey;
		this.queueTag = queueTag;
		
		
	}

	public void send(String data) {
//		rabbitTemplate.convertAndSend("NettyMqServerListenerExchange", "", data);
		;
		
		System.out.println("DATA BEFORE SENDING MQ SENDER: " + data);
		
		
		
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
