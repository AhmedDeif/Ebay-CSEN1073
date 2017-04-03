

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import server.EchoServerHandler;
import server.HeartBeatHandler;
import server.MqSender;
import server.ToMessageDecoder;

public class ServicesInitializer extends ChannelInitializer<SocketChannel> {

    protected final SslContext      _sslCtx;
    
    protected final Controller    _controller;
    
	private final MqSender _mqSender;


    public ServicesInitializer(SslContext sslCtx , Controller controller, MqSender sender) {
        _sslCtx       =   sslCtx;
        _controller   =   controller;
        _mqSender = sender;
    }

    @Override
    public void initChannel(SocketChannel socChannel) {
    
        CorsConfig corsConfig = CorsConfig.withAnyOrigin().build();
     
        ChannelPipeline pipeLine = socChannel.pipeline( );
        if(_sslCtx != null) {
            pipeLine.addLast( _sslCtx.newHandler( socChannel.alloc( ) ) );
        }
        
//		pipeLine.addLast(new HttpRequestDecoder( ) );
//        
//		// Uncomment the following line if you don't want to handle HttpChunks.
//        pipeLine.addLast(new HttpObjectAggregator(1048576));
//        
//		pipeLine.addLast(new HttpResponseEncoder());
//        
//		// Remove the following line if you don't want automatic content compression.
//        //p.addLast(new HttpContentCompressor());
//        pipeLine.addLast("1", new HttpStaticFileServerHandler( ) ); 
//        pipeLine.addLast(new CorsHandler(corsConfig));
//        pipeLine.addLast("2", new ServicesHandler( _controller ) );
        
		pipeLine.addLast(new IdleStateHandler(3 * 60, 0, 0));
		pipeLine.addLast(new HeartBeatHandler());
		pipeLine.addLast(
				new LengthFieldBasedFrameDecoder(65536, 0, 4, -4, 0));
		pipeLine.addLast(new ToMessageDecoder());
		pipeLine.addLast(new EchoServerHandler(_mqSender));


    }
}