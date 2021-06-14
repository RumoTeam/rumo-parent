package com.github.rumoteam.updater.client.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class ClientDemo {

	public static void main(String[] args) {

		EventLoopGroup clientWorkgroup = new NioEventLoopGroup();
		ClientDemo demo = new ClientDemo();

		Bootstrap clientBootstrap = demo.client();
		clientBootstrap.connect("127.0.0.1", 39543).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
				request.headers().set("TASK", "UPDATE");
				request.headers().set("PROJECT", "SaveVill");
				future.channel().writeAndFlush(request);
			}
		});

		clientWorkgroup.shutdownGracefully();
	}

	public Bootstrap client() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new HttpClientCodec());
				ch.pipeline().addLast(new HttpObjectAggregator(1048576));
				ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
						final String echo = msg.content().toString(CharsetUtil.UTF_8);
						System.out.println("Response: " + echo);

						ctx.flush();
						ctx.close();
					}
				});
			}
		});
		return b;
	}
}
