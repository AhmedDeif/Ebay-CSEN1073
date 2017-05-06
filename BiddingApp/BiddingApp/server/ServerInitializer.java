package BiddingApp.server;


import BiddingApp.client.MqSender;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    protected final Controller    _controller;

	private final MqSender _mqSender;


    public ServerInitializer( Controller controller, MqSender sender) {
        _controller   =   controller;
        _mqSender = sender;
    }

    @Override
    public void initChannel(SocketChannel socChannel) {

        CorsConfig corsConfig = CorsConfig.withAnyOrigin().build();

        ChannelPipeline pipeLine = socChannel.pipeline( );

        pipeLine.addLast( new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeLine.addLast(new StringDecoder());
        pipeLine.addLast(new StringEncoder());
        pipeLine.addLast("2", new ServerHandler( _controller ) );


    }
}
