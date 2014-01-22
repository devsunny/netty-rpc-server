package com.asksunny.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asksunny.protocol.rpc.RPCEnvelope;
import com.asksunny.protocol.rpc.StreamProtocolDecoder;
import com.asksunny.protocol.rpc.StreamProtocolEncoder;

public class RPCServerHandler extends
		SimpleChannelInboundHandler<byte[]> {

			final static Logger log = LoggerFactory.getLogger(RPCServerHandler.class);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			byte[] rawMessage) throws Exception 
	{

		ByteArrayInputStream bin = new ByteArrayInputStream(rawMessage);	
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		StreamProtocolDecoder decoder = new StreamProtocolDecoder();
		StreamProtocolEncoder encoder = new StreamProtocolEncoder();
		try{
			RPCEnvelope envelope = null;
			while ((envelope=decoder.decodeNow(bin))!=null) {
				try{
					RPCRuntime rt = RPCRuntimeFactory.getDefaultfactory().getRPCRuntime(envelope);				
					if(log.isDebugEnabled()) log.debug("found the match runtime {}", rt.getClass().getName());
					RPCEnvelope response = rt.invoke(envelope);
					if(response!=null){
						encoder.encode(bout, response);
						ctx.writeAndFlush(bout.toByteArray());
					}
				}catch(Throwable ex){
					log.error(String.format("Failed to execute client %s request %s", ctx.channel().remoteAddress(), envelope.toString()), ex);	
				}
			}			
		}catch(Exception ex){
			log.error(String.format("Failed to handle client %s request",ctx.channel().remoteAddress()), ex);			
		}finally{
			
		}		
		
	}
	
	
	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}
	
	
	

}
