package com.uestc.util;

public class RedisKeyUtil {
	private static String SPLIT=":";
	private static String BIZ_LIKE="LIKE";//点踩
	private static String BIZ_DISLIKE="DISLIKE";//点赞
	private static String BIZ_EVENT="EVENT";//异步事件
	private static String BIZ_NEWSCORE="NEWSCORE";//新闻的分数
	private static String BIZ_NEWSCOREMIRROR="NEWSCOREMIRROR";//新闻分数的备份
	public static String getEventQueueKey(){
		return BIZ_EVENT;
	}
	
	
	
	/**
	 * 点赞的key值生成
	 * @param entityId newsId
	 * @param entityType 类型：1
	 * @return
	 */
	public static String getLikeKey(int entityId,int entityType){
		return BIZ_LIKE+SPLIT+String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
	}
	/**
	 * 点踩的key值生成
	 * @param entityId
	 * @param entityType
	 * @return
	 */
	public static String getDislikeKey(int entityId,int entityType){
		return BIZ_DISLIKE+SPLIT+String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
	}
	/**
	 * 我的设计思想是少的访问数据库，然后在更新的时候我们通过两个redis的zset去捕捉这段事件变化的分数
	 * @return
	 */
	public static String getNewsScore(){
		return BIZ_NEWSCORE;
	}
	public static String getNewsScoreMirror(){
		return BIZ_NEWSCOREMIRROR;
	}
	
}
