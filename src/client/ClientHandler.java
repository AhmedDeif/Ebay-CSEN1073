package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import message.Message;
import server.MqReceiver;
import server.MqSender;

public class ClientHandler extends SimpleChannelInboundHandler<Object>{
	private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
	
	private MqSender _mqSender;
	private ChannelHandlerContext context;
	
	public ClientHandler (MqSender mqSender) {
		_mqSender = mqSender;
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
		log.info("CONNECTION ESTABLISHED " + ctx.name());
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
		// TODO Auto-generated method stub
		log.info("MESSAGE RECIEVED AT CLIENT: " + data);
		
		_mqSender.send(data.toString());
	}
}
