package client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class MqConsumer  extends DefaultConsumer {
	
	MqClientHandler _clientHandler;
	Channel _channel;
	private static final Logger log = LoggerFactory.getLogger(MqConsumer.class);

	
	public MqConsumer(Channel channel, MqClientHandler clientHandler) {
		super(channel);
		_clientHandler = clientHandler;
		_channel = channel;
	}
	
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) {
			
		try {
			String messageBody = new String(body);
			Gson gson = new Gson();
			JsonObject messageJson = gson.fromJson(messageBody, JsonObject.class);
			
			
			_clientHandler.send(messageJson).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture arg0) throws Exception {
					 _channel.basicAck(envelope.getDeliveryTag(), false);
					 log.info("MQ Reciever Acknowledge Message");				
				}
			});			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
