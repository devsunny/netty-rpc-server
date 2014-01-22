package com.asksunny.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class RPCAPIClientHandler extends
		SimpleChannelInboundHandler<byte[]> {

	
			@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			byte[] rawMessage) throws Exception 
	{
		
		
		
	}

}
