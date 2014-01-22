package com.asksunny.rpc.client;

import com.asksunny.protocol.rpc.RPCClientCallbackListener;
import com.asksunny.protocol.rpc.RPCEnvelope;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RPCClient 
{
	String remoteHost = "localhost";
	int port = 10040;
	EventLoopGroup group = null;
	Channel serverChannel = null;

	public RPCClient(String remoteHost, int port) {
		super();
		this.remoteHost = remoteHost;
		this.port = port;
	}

	public RPCClient(int port) {
		super();
		this.port = port;
	}

	public void openConnection() throws Exception {
		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
				.handler(new RPCClientChannelInitializer());
		final Channel ch  = b.connect(remoteHost, port).sync().channel();
		serverChannel = ch;
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ch.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}));

	}
	
	
	public void sendRequest(RPCEnvelope request, RPCClientCallbackListener clientListener)
	{
		
	}
	
	public RPCEnvelope sendRequest(RPCEnvelope request)
	{
		
		
		return null;
	}
	
	
	

	public void shutdown() throws Exception {
		try {
			serverChannel.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	

}
