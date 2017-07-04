package com.uestc.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uestc.dao.NewsDAO;
import com.uestc.model.HostHolder;
import com.uestc.model.News;
import com.uestc.util.ToutiaoUtils;

@Service
public class NewsService {
	@Autowired
	private NewsDAO  newsDAO;
	
	/**
	 * 获取最新的几个新闻
	 * @param userId
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<News> getLatestNews(int userId,int offset,int limit){
		return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
	}
	/**
	 * 上传新闻的图片
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String saveImage(MultipartFile file) throws IOException{
		int dotPos=file.getOriginalFilename().lastIndexOf(".");
		if(dotPos<0){
			return null;
		}
		String fileExt = file.getOriginalFilename().substring(dotPos+1);
		if(!ToutiaoUtils.isFileAllowed(fileExt)){
			return null;
		}
		String fileName =UUID.randomUUID().toString()+"."+fileExt;
		//java 的 nio
		Files.copy(file.getInputStream(),new File(ToutiaoUtils.IMAGE_DIR+fileName).toPath(),StandardCopyOption.REPLACE_EXISTING);
		return ToutiaoUtils.TOUTIAO_DOMAIN+"image?name="+fileName;
	}
	/**
	 * 分享添加新闻的链接
	 * @param news
	 * @return
	 */
	public int addNews(News news){

		newsDAO.addNews(news);

		return news.getId();
		
	}
	/**
	 * 通过id获取详细信息
	 * @param newId
	 * @return
	 */
	public News selectByNewId(int newId){
		return newsDAO.selectByNewsId(newId);
	}
	
	public int updateCommentCount(int newsId,int count){
		return  newsDAO.updateCommentCount(newsId, count);
	}
	public int updateLikeCount(int newsId,int count){
		return newsDAO.updateLikeCount(newsId, count);
	}
	
	
	
}
