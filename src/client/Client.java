package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	private static final Logger log = LoggerFactory.getLogger(Client.class);
	
	
	
	  public Client() {
	    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
	    Bootstrap b = new Bootstrap();
	    b.group(workerGroup);
	    b.channel(NioSocketChannel.class);
	 
	    b.handler(new ChannelInitializer<SocketChannel>() {
	      @Override
	      public void initChannel(SocketChannel ch) throws Exception {
	        ch.pipeline().addLast(new ClientHandler());
	      }
	    });
	     
	    String serverIp = "127.0.0.1";
        try {
			Channel ch = b.connect(serverIp, 3000).sync().channel();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
	    log.debug("CLIENT CONNECTED MF");
	  }
	}