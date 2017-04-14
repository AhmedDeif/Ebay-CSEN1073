package SearchApp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;

import SearchApp.client.MqClientHandler;
import SearchApp.client.MqConsumer;
import SearchApp.client.MqRecieverThread;

public class MqRecieverThread extends Thread {
	private static final Logger log = LoggerFactory.getLogger(MqRecieverThread.class);

	
	private Connection connection;
	private String exchangeName;
	private String queueName;
	private String routeKey;
	private String queueTag;
	private MqClientHandler clientHandler;
	
	
	public MqRecieverThread(Connection connection, String exchangeName, String queueName, String routeKey,
			String queueTag, MqClientHandler clientHandler) {
		super();
		this.connection = connection;
		this.exchangeName = exchangeName;
		this.queueName = queueName;
		this.routeKey = routeKey;
		this.queueTag = queueTag;
		this.clientHandler = clientHandler;
	}



	
	@Override
	public void run() {
		try {
			final Channel channel = connection.createChannel();
			channel.exchangeDeclare(exchangeName, "direct", true, false, null);
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchangeName, routeKey);

			// process the message one by one
			channel.basicQos(1);

			DefaultConsumer queueingConsumer = new MqConsumer(channel, clientHandler);
			// auto-ack is false
			channel.basicConsume(queueName, false, queueTag, queueingConsumer);
		} catch (Exception ex) {
			log.error(String.format("Create Rabbit MQ listener error %s", ex.getMessage()));
		}
	}
}
