package com.asksunny.rpc.server;

public interface RPCCallbackListener 
{
	void sendOutputLineResponse(String responseLine);
	void sendErrorResponse(String responseLine);
}
