package com.uestc.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

/**
 * 创建线程池
 * @author liukunsheng
 *
 */
@Component
public class SimpleExecutors {
	
	private static ThreadPoolExecutor executor;
	static{
		int computorCPU = Runtime.getRuntime().availableProcessors();
		int corePoolSize = computorCPU+1;
		int maxPoolSize = corePoolSize;
		long keepAliveTime =1;
		TimeUnit timeUnit = TimeUnit.HOURS;
		LinkedBlockingQueue<Runnable> workQueue= new LinkedBlockingQueue<>();
		executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, workQueue);	
	}
	
	public ThreadPoolExecutor getExecutor(){
		return executor;
	}
	
}
