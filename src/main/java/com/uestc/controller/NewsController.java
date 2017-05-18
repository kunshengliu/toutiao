package com.uestc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.uestc.service.NewsService;
import com.uestc.util.ToutiaoUtils;

@Controller
public class NewsController {
	private static final Logger logger=LoggerFactory.getLogger(NewsController.class);
	
	@Autowired
	private NewsService newsService;
	
	@RequestMapping(path={"/uploadImage/"},method={RequestMethod.POST})
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file){
		try {
			String fileUrl = newsService.saveImage(file);
			if(fileUrl==null){
				return ToutiaoUtils.getJSONString(1,"图片上传失败");				
			}
			
			return ToutiaoUtils.getJSONString(0, fileUrl);
			
		} catch (Exception e) {
			// TODO: handle exception
			//logger.error("图片上传失败："+e.getMessage());
			return ToutiaoUtils.getJSONString(1, "上传失败");	
		}
	}
	@RequestMapping("/wozenme1zhidoa1")
	@ResponseBody
	public void getImage(@RequestParam("name")String imageName,
			HttpServletResponse response){
		response.setContentType("image/jpeg");//设置图片格式
		try {
			StreamUtils.copy(new FileInputStream(new File(ToutiaoUtils.IMAGE_DIR+imageName)), 
					response.getOutputStream());
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
