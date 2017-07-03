package com.uestc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * jedis的
 * @author liukunsheng
 *
 */
@Component
public class JedisAdapter implements InitializingBean{
	private Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
	private JedisPool pool =null;
	/**
	 * 用于初始化Bean
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		pool = new JedisPool("localhost",6379);
	}
	public Jedis getJedis(){
		return pool.getResource();
	}
	/**
	 * 添加 K－V建值对
	 * @param key
	 * @param value
	 * @return
	 */
	public long sadd(String key,String value){
		Jedis jedis=null;
		try {
			jedis=this.getJedis();
			return jedis.sadd(key, value);
		} catch (Exception e) {
			logger.error("redis happens something error:"+e.getMessage());
			return 0L;
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		
	}
	/**
	 * 移除
	 * @param key
	 * @param value
	 * @return
	 */
	public long srem(String key,String value){
		Jedis jedis=null;
		try {
			jedis=this.getJedis();
			return jedis.srem(key, value);
		} catch (Exception e) {
			logger.error("redis happens something error:"+e.getMessage());
			return 0L;
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		
	}
	
	
}
