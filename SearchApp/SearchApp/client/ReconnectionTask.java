package SearchApp.client;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ReconnectionTask implements Runnable, ChannelFutureListener {
    Channel previous;

    public ReconnectionTask(Channel c) {
        this.previous = c;
    }

    @Override
    public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(new MqClientInitializer());
         b.remoteAddress(previous.remoteAddress())
          .connect()
          .addListener(this);
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()) {
            // Will try to  again in 100 ms.
            previous.eventLoop()
                    .schedule(this, 100, TimeUnit.MILLISECONDS); 
            return;
        }
    }
}