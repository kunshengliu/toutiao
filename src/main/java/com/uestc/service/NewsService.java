package com.uestc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.dao.NewsDAO;
import com.uestc.model.News;

@Service
public class NewsService {
	@Autowired
	private NewsDAO  newsDAO;
	
	public List<News> getLatestNews(int userId,int offset,int limit){
		return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
	}
	
	
	
}
