package com.asksunny.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServerBoostrap {

	int port = 10040;
	
	
	public void run(String[] args) throws Exception
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new RPCServerChannelInitializer());
             final ChannelFuture ch = b.bind(port).sync().channel().closeFuture();
             Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {				
				@Override
				public void run() {
					try{
					ch.channel().close();	
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}));             
             if(ch!=null){
            	 ch.sync();
             }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		NettyServerBoostrap server = new NettyServerBoostrap();
		server.run(args);
	}

}
