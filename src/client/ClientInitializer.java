package client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import server.MqSender;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

	MqSender _mqSender;
	
    public ClientInitializer(MqSender mqSender) {
        _mqSender = mqSender;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new ClientHandler(_mqSender));
    }
}