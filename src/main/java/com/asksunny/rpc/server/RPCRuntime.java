package com.asksunny.rpc.server;

import java.util.concurrent.ExecutorService;

import com.asksunny.protocol.rpc.RPCEnvelope;

public interface RPCRuntime 
{
	public boolean accept(RPCEnvelope envelope)  throws Exception;
	public RPCEnvelope invoke(RPCEnvelope envelope)  throws Exception;
	public void setExecutorService( ExecutorService executor)  throws Exception;
	public void setCallbackListener(RPCCallbackListener listener) throws Exception;
	
}
