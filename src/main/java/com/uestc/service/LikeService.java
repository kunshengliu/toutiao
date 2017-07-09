package com.uestc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.util.JedisAdapter;
import com.uestc.util.RedisKeyUtil;

@Service
public class LikeService {
	@Autowired
	private JedisAdapter jedisAdapter;
	/**
	 * 如果喜欢就返回1，如果不喜欢就返回－1，否则返回0
	 * @param useId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public int getLikeStatus(int userId,int entityType,int entityId){
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		if(jedisAdapter.sismember(likeKey, String.valueOf(userId))){
			return 1;
		}
		String disLikeKey =RedisKeyUtil.getLikeKey(entityId, entityType);
		if(jedisAdapter.sismember(disLikeKey, String.valueOf(userId))){
			return -1;
		}
		return 0;
	}
	/**
	 * 有人点赞
	 * @param userId
	 * @param entityType 实体是news
	 * @param entityId  newsId；
	 * @return
	 */
	public long like(int userId,int entityType,int entityId){
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		jedisAdapter.sadd(likeKey, String.valueOf(userId));
		
		String disLikeKey =RedisKeyUtil.getDislikeKey(entityId, entityType);
		jedisAdapter.srem(disLikeKey, userId+"");
		return jedisAdapter.scard(likeKey);//返回点赞数量
	}
	
	public long disLike(int userId,int entityType,int entityId){
		String disLikeKey =RedisKeyUtil.getDislikeKey(entityId, entityType);
		jedisAdapter.sadd(disLikeKey, userId+"");
		
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		jedisAdapter.srem(likeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
	}
	
	
	
	
	
	
}
