package com.uestc.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.uestc.model.NewsScore;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;

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
	 * 添加 set 集合的元素
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
	 * 移除set里面的元素
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
	 * 查看set集合里面是不是有这个元素
	 * @param key 集合名字
	 * @param value userId
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
	//redis
	public void setObject(String key,Object object){
		set(key,JSON.toJSONString(object));
	}
	
	public void set(String key,String value){
		Jedis jedis =null;
		try {
			jedis =pool.getResource();
			jedis.set(key, value);
			
		} catch (Exception e) {
			logger.error("redis has someting error:"+e.getMessage());
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	
	
	public <T>T getObject(String key,Class<T> clazz){
		String value =get(key);
		if(value!=null){
			return JSON.parseObject(value,clazz);
		}
		return null;
	}
	
	public String get(String key){
		Jedis jedis =null;
		try {
			jedis=pool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
			logger.error("发生异常"+e.getMessage());
			return null;
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	//
	public long lpush(String key,String value){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("发生异常"+e.getMessage());
			return 0;
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	/**
	 * 阻塞的取出来
	 * @param timeout 我们默认设置为0
	 * @param key
	 * @return
	 */
	public List<String> brpop(int timeout,String key){
		Jedis jedis = null;
		try {
			jedis =pool.getResource();
			return jedis.brpop(timeout,key);
		} catch (Exception e) {
			logger.error("发生异常"+e.getMessage());
			return null;
		}finally {
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	/**
	 * 
	 * @param key
	 * @param value
	 */
   public void zincrby(String key,String value){
	   Jedis jedis =null;
	   try{
		   jedis =pool.getResource();
		   jedis.zincrby(key, 1, value);
	   }catch(Exception e){
		   logger.error("redis 发生异常");
	   }finally {
		if(jedis!=null){
			jedis.close();
		}
	}
   }
   /**
    * 取出zset里面所有元素
    * @param key
    * @return
    */
   public Set<Tuple> zrange (String key){
	   Jedis jedis =null;
	   try {
		jedis = pool.getResource();
		//jedis.zrangeWithScores(key, start, end)
	   Set<Tuple> result=jedis.zrangeWithScores(key, 0, -1);
	   return result;
	} catch (Exception e) {
		logger.error("redis 发生异常"+e.getMessage());
		return null;
	}finally {
		if(jedis!=null){
			jedis.close();
		}
	 }
   }
   public double zscore(String key ,String member){
	   Jedis jedis=null;
	   try {
		jedis =pool.getResource();
		return jedis.zscore(key, member);
	} catch (Exception e) {
		logger.error("redis error"+e.getMessage());
		return 0;
	}finally {
		if(jedis!=null){
			jedis.close();
		 }
	 }
	}
   
   public void zadd(String key,String member,double score){
	   Jedis jedis =null;
	   try {
		jedis = pool.getResource();
		//jedis.z
	} catch (Exception e) {
		// TODO: handle exception
	 }
   }
   
   public void batchUpdateScore(String key , Map<String,Double> map){
	   Jedis jedis =null;
	   try {
		   jedis=pool.getResource();
		   jedis.zadd(key, map);
		   
	} catch (Exception e) {
		logger.error("redis exception");
	}finally {
		if(jedis!=null){
			jedis.close();
		}
	}
	   
	   
	   
   }
	
	
}
