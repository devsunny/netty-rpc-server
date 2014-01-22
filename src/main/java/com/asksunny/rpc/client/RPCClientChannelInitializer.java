package com.asksunny.rpc.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class RPCClientChannelInitializer extends
		ChannelInitializer<SocketChannel> {

	public final static int LENGTH_FIELD_LENGTH = 4;
	public final static int MAX_MSG_LENGTH = 1024 * 1024 * 64; // 64MB;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(MAX_MSG_LENGTH, 0, 4, 0, 4));
		p.addLast("rawMessageDecoder", new ByteArrayDecoder());
		p.addLast("frameEncoder", new LengthFieldPrepender(4));
		p.addLast("rawMessageEncoder", new ByteArrayEncoder());
		p.addLast("handler", new RPCClientHandler());

	}

}
