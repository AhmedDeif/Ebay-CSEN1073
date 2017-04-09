

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.Client;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

public class ServicesHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger log = LoggerFactory.getLogger(ServicesHandler.class);

	private HttpRequest request;

	/** Buffer that stores the response content */
	protected StringBuilder buf = new StringBuilder();

	protected Controller _controller;

	public ServicesHandler(Controller controller) {
		_controller = controller;
	}

	public void setResponse(StringBuffer strbufResponse) {
		if (strbufResponse == null)
			buf = null;
		else {
			buf.setLength(0);
			buf.append(strbufResponse.toString());
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public boolean acceptInboundMessage(Object msg) throws Exception {
//		HttpRequest request;
//
//		request = (HttpRequest) msg;
//		if (request.method().compareTo(HttpMethod.POST) == 0)
//			return true;
//		return false;
		return true;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) {

		System.err.println(" got a request: " + msg);
		
			log.info("SERVER GOT MESSAGE NOW SENDING TO CLIENT..");
			ctx.writeAndFlush(msg);
//			TODO: CHECK IF MESSAGE JSON CALL EXECUTE COMMAND
//			_controller.execRequest(new ClientHandle(ctx, request, this));


//		if (msg instanceof HttpRequest) {
//
//			this.request = (HttpRequest) msg;
//			HttpRequest request = this.request;
//			if (request.method().compareTo(HttpMethod.POST) == 0) {
//				// it is a POST -- nice
//				try {
//					System.err.println(" got a post: " + request.toString());					
//					_controller.execRequest(new ClientHandle(ctx, request, this));
//					synchronized (this) {
//						this.wait();
//					}
//					if (buf != null) {
//						System.err.println(" sending back" + buf.toString());
//						writeResponse(request, ctx);
//						ctx.close();
//					} else {
//						System.err.println(" Got a bad request. Closing channel ");
//						ctx.close();
//					}
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//			
//			//			Check for http get method
//		}
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpHeaderUtil.setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		/*
		 * if (!HttpHeaderUtil.isKeepAlive(req) || res.status().code() != 200) {
		 * f.addListener(ChannelFutureListener.CLOSE); }
		 */
	}

	private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
		// Decide whether to close the connection or not.
		boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
		// Build the response object.
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				currentObj.decoderResult().isSuccess() ? OK : BAD_REQUEST,
				Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

		if (keepAlive) {
			// Add 'Content-Length' header only for a keep-alive connection.
			response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
			// Add keep alive header as per:
			// -
			// http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
			response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}

		// Write the response.
//		ctx.write(response);
		sendHttpResponse(ctx, request, response);

		return keepAlive;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}