package com.uestc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.uestc.model.EntityType;
import com.uestc.model.HostHolder;
import com.uestc.model.News;
import com.uestc.model.ViewObject;
import com.uestc.service.LikeService;
import com.uestc.service.NewsService;
import com.uestc.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LikeService likeService;
	
	
	@Autowired
	private HostHolder hostHolder;
	
	@RequestMapping(path={"/","/index"},method={RequestMethod.GET,RequestMethod.POST})
	public String index(Model model,
			@RequestParam(value="pop",required=false, defaultValue="0") int pop){
		System.out.println("this is index");
		model.addAttribute("vos",getNews(0,0,10));
		model.addAttribute("pop", pop);
		return "home";
	}

	public List<ViewObject> getNews(int userId,int offset,int limit){
		List<News> newsList = newsService.getLatestNews(userId, offset, limit);
		int localUserId = hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
		List<ViewObject> vos = new ArrayList<ViewObject>();
		for(News news:newsList){
			ViewObject vo = new ViewObject();
			vo.set("news",news);
			vo.set("user",userService.getUser(news.getUserId()));
			if(localUserId!=0){
				vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
			}else{
				vo.set("like",0);
			}
			vos.add(vo);
			}	
		return vos;
	}
	
	@RequestMapping("/user/{userId}")
	public String userIndex(Model model,@PathVariable("userId") int userId){
		model.addAttribute("vos", getNews(userId, 0, 10));
		return "home";
	}
	
	
	
	
}
