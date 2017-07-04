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
	 * 添加 set K－V建值对
	 * @param key
	 * @param value
	 * @return
	 */
	public long sadd(String key,String value){
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			System.out.println(key+"   "+value);
			return jedis.sadd(key,value);
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
			jedis=pool.getResource();
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
	/**
	 * 查看是不是有这个k－v元素
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean sismember(String key,String value){
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error("redis happens something error:"+e.getMessage());
			return false;
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long scard(String key){
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.scard(key);
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
