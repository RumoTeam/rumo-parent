package com.github.rumoteam.updater.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class ServerInit {
	int port = 39543;

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup serverWorkgroup = new NioEventLoopGroup();
		ServerInit serverInit = new ServerInit();

		Channel serverChannel = serverInit.server(serverWorkgroup).sync().channel();
		int PORT = ((InetSocketAddress) serverChannel.localAddress()).getPort();
		System.err.println(PORT);
	}

	public ChannelFuture server(EventLoopGroup workerGroup) {
		ServerBootstrap b = new ServerBootstrap();
		b.group(workerGroup).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new HttpServerCodec());
						ch.pipeline().addLast(new HttpObjectAggregator(1048576));
						ConnectionHandler handler = new ConnectionHandler();
						ch.pipeline().addLast(handler);
					}
				});

		return b.bind();
	}
}
