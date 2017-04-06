package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import client.ClientHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * RabbitMQ Receiver based on RabbitMQ java client API
 */
public class MqReceiver {
	private static final Logger log = LoggerFactory.getLogger(MqReceiver.class);
	private ConnectionFactory connnectionFactory;
	private String exchangeName = "NettyMqServerListenerExchange";
	private String queueName = "MqListenerQueue";
	private String routeKey = "MqListenerQueue";

	private Thread listenThread;

	private ClientHandler clientHandler;

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
		listenThread = new Thread() {
			@Override
			public void run() {
				try {
					Connection connection = connnectionFactory.newConnection();

					final Channel channel = connection.createChannel();
					channel.exchangeDeclare(exchangeName, "direct", true, false, null);
					channel.queueDeclare(queueName, true, false, false, null);
					channel.queueBind(queueName, exchangeName, routeKey);

					// process the message one by one
					channel.basicQos(1);

					QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
					// auto-ack is false
					channel.basicConsume(queueName, false, queueingConsumer);
					while (true) {
						final QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
						String message = new String(delivery.getBody());
						broadcastMsgAndAck(message, channel, delivery);
					}
				} catch (Exception ex) {
					log.error(String.format("Create Rabbit MQ listener error %s", ex.getMessage()));
				}
			}
		};

		listenThread.setDaemon(true);
		listenThread.start();
	}

	private void broadcastMsgAndAck(String message, final Channel channel, final QueueingConsumer.Delivery delivery) {
		// Broadcast message to all connected clients
		// If you want to send to a specified client, just add
		// your own logic and ack manually
		// Be aware that ChannelGroup is thread safe
		// Client client = new Client();

		// NioSocketChannel clientChannel = new NioSocketChannel();
		// clientChannel.connect("127.0.0.1:3000");

		// if (EchoServerHandler.channels != null) {
		// log.info(String.format("Connected client number: %d",
		// EchoServerHandler.channels.size()));
		//
		//
		// log.info("RECIEVED MESSAGE: " + message);
		// ByteBuf msg = Unpooled.copiedBuffer(message.getBytes());
		// EchoServerHandler.channels.writeAndFlush(msg).addListener(new
		// ChannelGroupFutureListener() {
		// public void operationComplete(ChannelGroupFuture arg0) throws
		// Exception {
		// // manually ack to MQ server
		// // when message is consumed.
		// channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		// log.debug("Mq Receiver get message");
		// }
		// });
		// }
		
		log.info("MQ MESSAGE Listen: " + message) ;

		clientHandler.send(message).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture arg0) throws Exception {
				log.info(arg0.toString());
				 channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				 log.info("MQ Reciever Acknowledge Message");				
			}
		});

	}
}
