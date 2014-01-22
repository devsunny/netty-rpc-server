package com.asksunny.rpc.javart;

import java.util.List;

import com.asksunny.protocol.rpc.RPCObject;
import com.asksunny.rpc.server.RPCCallbackListener;

public interface RPCWorkerInterface 
{
	List<RPCObject> execute(List<RPCObject> args) throws Exception;
	void setCallbackListener(RPCCallbackListener callbackListener) throws Exception;
}
