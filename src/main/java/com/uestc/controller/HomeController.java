package com.uestc.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uestc.model.News;
import com.uestc.model.ViewObject;
import com.uestc.service.NewsService;
import com.uestc.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path={"/","/index"},method={RequestMethod.GET,RequestMethod.POST})
	public String index(Model model){
		List<News> newsList = newsService.getLatestNews(0, 0, 10);
		
		List<ViewObject> vos = new ArrayList<>();
		for(News news:newsList){
			ViewObject vo = new ViewObject();
			vo.set("news",news);
			vo.set("user",userService.getUser(news.getUserId()));
			vos.add(vo);
		}
		model.addAttribute("vos",vos);
		//
		//List<ViewObjct>	
		return "home";
	}
}
