package com.asksunny.rpc.javart;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asksunny.io.utils.StrackTraceUtil;
import com.asksunny.protocol.rpc.JavaMainParamRPCObjectFormatter;
import com.asksunny.protocol.rpc.RPCEnvelope;
import com.asksunny.protocol.rpc.RPCJavaEnvelope;
import com.asksunny.protocol.rpc.RPCObject;
import com.asksunny.protocol.rpc.RPCShellEnvelope;
import com.asksunny.rpc.server.RPCCallbackListener;
import com.asksunny.rpc.server.RPCRuntime;

public class JavaRPCRuntime implements RPCRuntime {

	final static Logger log = LoggerFactory.getLogger(JavaRPCRuntime.class);
	
	ExecutorService executorService;
	RPCCallbackListener callbackListener;
	
	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public RPCEnvelope invoke(RPCEnvelope request) throws Exception {
		
		RPCJavaEnvelope javEnv = (RPCJavaEnvelope)request;
		List<RPCObject> params = javEnv.getRpcObjects();		
		RPCShellEnvelope response = new RPCShellEnvelope();
		response.setRpcType(RPCEnvelope.RPC_TYPE_RESPONSE);
		
		RPCDynamicClassLoader cl = RPCDynamicClassLoader
				.getClassLoader((byte[]) javEnv.getJarSource());
		try {
			Class<?> clazz = Class.forName(javEnv.getClassName(), false, cl);
			Object instance = clazz.newInstance();
			
			if (instance instanceof Runnable) {
				if(log.isDebugEnabled() ) log.debug("Instance of Runnanle");
				try {
					if(getExecutorService()!=null){
						getExecutorService().submit((Runnable)instance);
					}else{
						((Runnable)instance).run();
					}
					response.addRpcObjects(0);
				} catch (Throwable t) {
					String msg = StrackTraceUtil.getStackTraceAsString(t);
					response.addRpcObjects(1);
					response.addRpcObjects(msg);
				}
			} else if (instance instanceof RPCWorkerInterface) {
				if(log.isDebugEnabled() ) log.debug("Instance of RPCWorkerInterface");
				RPCWorkerInterface worker = (RPCWorkerInterface) instance;
				List<RPCObject> result = worker.execute(params);
				response.setRpcObjects(result);
			} else if (hasMain(clazz)) {
				if(log.isDebugEnabled() ) log.debug("Class with Main Method.");
				try {
					JavaMainParamRPCObjectFormatter formatter = new JavaMainParamRPCObjectFormatter();					
					Method m = clazz.getMethod("main", String[].class);
					m.invoke(null, new Object[]{formatter.formatJavaMainArgs(javEnv)});
					response.addRpcObjects(0);
				} catch (Throwable t) {
					String msg = StrackTraceUtil.getStackTraceAsString(t);
					response.addRpcObjects(1);
					response.addRpcObjects(msg);
				}
			}

		} finally {
			cl.close();
		}
		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean hasMain(Class clazz) {
		try {
			Method m = clazz.getMethod("main", String[].class);
			if (m != null) {
				return true;
			}
		} catch (Exception e) {
			;
		}
		return false;

	}

	public boolean accept(RPCEnvelope envelope) throws Exception {
		return envelope.getEnvelopeType() == RPCEnvelope.RPC_ENVELOPE_TYPE_JAVA;
	}

	public RPCCallbackListener getCallbackListener() {
		return callbackListener;
	}

	public void setCallbackListener(RPCCallbackListener callbackListener) {
		this.callbackListener = callbackListener;
	}

	
}
