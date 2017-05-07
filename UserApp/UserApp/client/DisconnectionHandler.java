package UserApp.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DisconnectionHandler extends SimpleChannelInboundHandler {

  @Override
  public void channelInactive(final ChannelHandlerContext ctx) throws Exception   {

    Channel channel = ctx.channel();

    /* If shutdown is on going, ignore */
    if (channel.eventLoop().isShuttingDown()) return;

    ReconnectionTask reconnect = new ReconnectionTask(channel);
    reconnect.run();
  }

@Override
protected void messageReceived(ChannelHandlerContext arg0, Object arg1) throws Exception {
	// TODO Auto-generated method stub
	
}

}