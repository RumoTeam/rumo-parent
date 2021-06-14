package com.github.rumoteam.updater.server;

import java.util.List;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class ConnectionHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		String uriString = request.uri();

		StringBuilder stringBuilder = new StringBuilder();

		String project = null;
		String task = null;
		stringBuilder.append("URL:").append(uriString).append('\n');

		HttpHeaders headers = request.headers();

		List<Entry<String, String>> heads = headers.entries();
		for (Entry<String, String> entry : heads) {
			String key = entry.getKey();
			String val = entry.getValue();
			if (key.equals("PROJECT")) {
				project = val;
			}
			if (key.equals("TASK")) {
				task = val;
			}
		}
		stringBuilder.append("PROJECT:").append(project).append('\n');

		if (task == null) {
			DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
					HttpResponseStatus.BAD_REQUEST);

			ctx.write(response);
			ctx.flush();
			ctx.close();
			System.err.println("task==null");
			return;
		}

		// update

		if (task.equals("UPDATE")) {

		}
		System.err.println(stringBuilder);

		// Files.hash(new File(fileName), Hashing.sha512()).toString();
		// PROJECT/file.jar

		final ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);

		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				content.copy());

		ctx.write(response);
		ctx.flush();
		ctx.close();
	}
}