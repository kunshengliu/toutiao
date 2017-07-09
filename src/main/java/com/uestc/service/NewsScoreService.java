package com.uestc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.util.JedisAdapter;
import com.uestc.util.RedisKeyUtil;

/**
 * 修改新闻热度
 * @author liukunsheng
 *
 */
@Service
public class NewsScoreService {
	@Autowired
	private JedisAdapter jedisAdapter;
	/**
	 * 修改新闻的热度
	 * @param newsId
	 */
	public void updateNewsScore(int newsId){
		jedisAdapter.zincrby(RedisKeyUtil.getNewsScore(), newsId+"");
	}
	
}
