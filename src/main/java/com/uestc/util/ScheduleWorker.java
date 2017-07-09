package com.uestc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.uestc.dao.NewsDAO;
import com.uestc.model.NewsScore;

import redis.clients.jedis.Tuple;

/**
 * 周期执行任务
 * @author liukunsheng
 *
 *
 */
@Component
public class ScheduleWorker {
	@Autowired
	private JedisAdapter jedisAdapter;
	@Autowired
	private NewsDAO newsDAO;
	
	
	@Autowired
	private SimpleExecutors simpleExecutors;
	
	
	
    @Scheduled(cron = "0 */1 * * *")
	public void updateNewsScore(){
		Set<Tuple> set = jedisAdapter.zrange(RedisKeyUtil.getNewsScore());
		//下面是对redis镜像里面的分数进行更新，同时找出需要更新的新闻
		//Map<String,Double> map = new HashMap<>();
		List<NewsScore> list = new ArrayList<>();
		Map<String,Double> map = new HashMap<String,Double>();
		for(Tuple t:set){
			String key =t.getElement();
			double score=t.getScore();
			double oldScore = jedisAdapter.zscore(RedisKeyUtil.getNewsScoreMirror(), key);
			if(score>oldScore){
				NewsScore newScore = new NewsScore();
				newScore.setNewsId(Integer.parseInt(key));
				newScore.setScore(score);
				list.add(newScore);
				map.put(key, score);
			}
		}
		//现在进行两个事情，一个是对数据库今夕功能更新，另外一个是把mirror里面的数据进行更新
		
		simpleExecutors.getExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				if(map.size()>0){
					jedisAdapter.batchUpdateScore(RedisKeyUtil.getNewsScoreMirror(), map);
				}
			}
		});
		
		
		if(list.size()>0){
			newsDAO.updateScoreBatch(list);
		}
		
		
		
		
		
	}
	
	
}
