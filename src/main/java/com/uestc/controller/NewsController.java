package com.uestc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.uestc.async.EventModel;
import com.uestc.async.EventProducer;
import com.uestc.async.EventType;
import com.uestc.model.Comment;
import com.uestc.model.EntityType;
import com.uestc.model.HostHolder;
import com.uestc.model.News;
import com.uestc.model.User;
import com.uestc.model.ViewObject;
import com.uestc.service.CommentService;
import com.uestc.service.NewsScoreService;
import com.uestc.service.NewsService;
import com.uestc.service.QiniuService;
import com.uestc.service.UserService;
import com.uestc.util.SimpleExecutors;
import com.uestc.util.ToutiaoUtils;

@Controller
public class NewsController {
	private static final Logger logger=LoggerFactory.getLogger(NewsController.class);
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private QiniuService qiniuService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private CommentService commentService;
	
	
	@Autowired
	private EventProducer eventProducer;
	
	
	@Autowired
	private SimpleExecutors simpleExecutors;
	
	@Autowired
	private NewsScoreService newsScoreService;
	
	/**
	 * 上传图片到云服务器
	 * @param file
	 * @return
	 */
	@RequestMapping(path={"/uploadImage/"},method={RequestMethod.POST})
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file){
		try {
			String fileUrl = qiniuService.upLoadSaveImage(file);
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
	/***
	 * 获取文件名字
	 * @param imageName
	 * @param response
	 */
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
	
	/**
	 * 上传新闻链接
	 * @param image
	 * @param title
	 * @param link
	 * @return
	 */
	@RequestMapping("/user/addNews/")
	@ResponseBody
	public String addNews(@RequestParam("image") String image,
			@RequestParam("title") String title,
			@RequestParam("link") String link){
		
		News news = new News();
		news.setImage(image);
		news.setLink(link);
		news.setCreatedDate(new Date());
		news.setDate(new Date());
		news.setTitle(title);
		news.setScore(0);
		User user = hostHolder.getUser();
		if(user!=null){
			//登陆用户
			news.setUserId(user.getId());
		}else{
			//匿名用户
			news.setUserId(-1);
		}
		try {
			newsService.addNews(news);
			return ToutiaoUtils.getJSONString(0,"新闻添加成功");
			
		} catch (Exception e) {
			logger.error("新闻上传失败"+e.getMessage());
			return ToutiaoUtils.getJSONString(1,"新闻上传失败");

		}
	}
	//news/2
	
	@RequestMapping("/news/{newsId}")
	public String newsDetail(@PathVariable("newsId") int newId,Model model){
		News news=  newsService.selectByNewsId(newId);
		if(news!=null){
			//加载评论
			List<Comment> comments = commentService.getCommentsByEntity(newId, EntityType.ENTITY_NEWS);
			
			List<ViewObject> commentVos = new ArrayList<>();
			
			for(Comment comment :comments){
				ViewObject vo = new ViewObject();
				vo.set("comment", comment);
				vo.set("user", userService.getUser(comment.getUserId()));
				commentVos.add(vo);
			}
			model.addAttribute("comments", commentVos);
		}
		model.addAttribute("news",news);
		model.addAttribute("owner",userService.getUser(news.getUserId()));
		System.out.println("detail2");
		return "detail";
	}
	/**
	 * 添加评论,并对评论数量进行更新
	 * @param newsId
	 * @param content
	 * @return
	 */
	@RequestMapping("/addComment")
	public String addComment(@RequestParam("newsId") int newsId,
			@RequestParam("content") String content){
		try{
			Comment comment = new Comment();
			comment.setUserId(hostHolder.getUser().getId());
			comment.setContent(content);
			comment.setEntityId(newsId);
			comment.setEntityType(EntityType.ENTITY_NEWS);
			comment.setCreatedDate(new Date());
			comment.setStatus(0);
			commentService.addComment(comment);
			News news = newsService.selectByNewsId(newsId);
			//更新数量，可以使用异步实现,异步实现很重要啊!
			// 现在进行异步化
			
			//使用线程池进行异步
			simpleExecutors.getExecutor().execute(new Runnable() {
				@Override
				public void run() {	
					newsScoreService.updateNewsScore(newsId);
					eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(hostHolder.getUser().getId())
							.setEntityId(newsId)
							.setEntityType(EntityType.ENTITY_COMENT)
							.setEntityOwnerId(news.getUserId()));
					int count = commentService.getCommentCount(newsId,EntityType.ENTITY_NEWS);//评论的数量
		            newsService.updateCommentCount(comment.getEntityId(), count);	
				}
			});	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "redirect:/news/" + String.valueOf(newsId);
	}
	
	
}
