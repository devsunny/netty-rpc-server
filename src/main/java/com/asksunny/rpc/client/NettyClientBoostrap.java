package com.asksunny.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class NettyClientBoostrap {

	int port = 10040;
	String host = "localhost";
	
	public void run(String[] args) throws Exception
	{
		 EventLoopGroup group = new NioEventLoopGroup();
	        try {
	            Bootstrap b = new Bootstrap();
	            b.group(group)
	             .channel(NioSocketChannel.class)
	             .handler(new RPCClientChannelInitializer());
	            // Make a new connection.
	            Channel ch = b.connect(host, port).sync().channel();	            
	            
	            byte[]  rawmessage = "This is a dum message".getBytes(CharsetUtil.UTF_8);
	            ch.writeAndFlush(rawmessage);
	            System.out.println(rawmessage.length + " sent.");
	           
	            
	            ch.closeFuture().sync();	            
	        } finally {
	            group.shutdownGracefully();
	        }
	}
	
	
	public static void main(String[] args) throws Exception {
		NettyClientBoostrap client = new NettyClientBoostrap();
		client.run(args);

	}

}
