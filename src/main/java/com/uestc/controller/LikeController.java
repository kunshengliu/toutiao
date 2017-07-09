package com.uestc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.uestc.async.EventModel;
import com.uestc.async.EventProducer;
import com.uestc.async.EventType;
import com.uestc.model.EntityType;
import com.uestc.model.HostHolder;
import com.uestc.model.News;
import com.uestc.service.LikeService;
import com.uestc.service.NewsScoreService;
import com.uestc.service.NewsService;
import com.uestc.util.SimpleExecutors;
import com.uestc.util.ToutiaoUtils;
/**
 * 点赞点踩
 * @author liukunsheng
 *
 */
@Controller
public class LikeController {
	@Autowired
	private LikeService likeService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	private SimpleExecutors simpleExecutors;
	
	@Autowired
	private NewsScoreService newsScoreService;
	
	@RequestMapping("/like")
	@ResponseBody
	public String like(@RequestParam("newsId") int newsId){
		int userId = hostHolder.getUser().getId();
		long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
		newsService.updateLikeCount(newsId,(int)likeCount);
		News news =newsService.selectByNewsId(newsId);
		simpleExecutors.getExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				//修改新闻热度
				//异步化处理
				newsScoreService.updateNewsScore(newsId);
				
				eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(userId).setEntityId(newsId)
						.setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));
			}
		});
		
		
		

		
		
		return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
	}
	@RequestMapping("/dislike")
	@ResponseBody
	public String dislike(@RequestParam("newsId") int newsId){
		int userId = hostHolder.getUser().getId();
		long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
		newsService.updateLikeCount(newsId,(int)likeCount);
		return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
	}
	
	
	
}
