package MerchantApp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP.BasicProperties;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MqClientHandler extends SimpleChannelInboundHandler<Object>{
	private static final Logger log = LoggerFactory.getLogger(MqClientHandler.class);
	
	private MqSender _mqSender;
	private ChannelHandlerContext context;
	
	public MqClientHandler () {
		_mqSender = new MqSender();
	}
	
	private void startMqListener(ChannelHandlerContext channel) {
		System.out.println("START MQ LISTENER");
		MqReceiver mqReceiver = new MqReceiver(this);
		mqReceiver.start();
	}
	
	public ChannelFuture send(Object data) {
		String message = data.toString();		
		return context.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		context = ctx;
		startMqListener(ctx);
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object data) throws Exception {	
		
		String messageString = (String) data;
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(messageString).getAsJsonObject();
		JsonElement propertiesJson = obj.get("properties");
		obj.remove("properties");
		
		Gson gson = new Gson();
		
		BasicProperties properties = gson.fromJson(propertiesJson, BasicProperties.class);
		
		
		_mqSender.send(gson.toJson(obj), properties);
	}
}
