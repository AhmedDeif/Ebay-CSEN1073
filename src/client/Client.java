package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import server.MqReceiver;
import server.MqSender;

public class Client {
	private static final Logger log = LoggerFactory.getLogger(Client.class);
	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", "3000"));

	protected static MqSender _mqSender;

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		_mqSender = new MqSender();

		try {

			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(_mqSender));

			// Start the connection attempt.
			ChannelFuture channelFuture = b.connect(HOST, PORT).sync();
			Channel ch = channelFuture.channel();
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// The connection is closed automatically on shutdown.
			group.shutdownGracefully();
		}
	}
}