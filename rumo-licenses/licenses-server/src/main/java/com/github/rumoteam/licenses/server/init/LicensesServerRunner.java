package com.github.rumoteam.licenses.server.init;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class LicensesServerRunner {

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap(); // (2)

		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addFirst("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						ch.pipeline().addAfter("decoder", "encoder", new ObjectEncoder());
						ch.pipeline().addLast(new ConnectionHandler());
					};
				}).option(ChannelOption.SO_REUSEADDR, true).option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
		int port = 1337;
		ChannelFuture f = b.bind(port).sync();
	}

}
