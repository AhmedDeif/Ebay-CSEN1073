package MerchantApp.client;

import MerchantApp.client.MqClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class MqClientInitializer extends ChannelInitializer<SocketChannel> {

	
    public MqClientInitializer() {
        
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeLine = ch.pipeline();

        pipeLine.addLast(new StringDecoder());
        pipeLine.addLast(new StringEncoder());
        pipeLine.addLast(new MqClientHandler());
    }
}