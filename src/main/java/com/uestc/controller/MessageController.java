package com.uestc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.model.HostHolder;
import com.uestc.model.Message;
import com.uestc.model.User;
import com.uestc.model.ViewObject;
import com.uestc.service.MessageService;
import com.uestc.service.UserService;
import com.uestc.util.ToutiaoUtils;
/**
 * 消息中中心
 * @author liukunsheng
 *
 */
@Controller
public class MessageController {
	private Logger logger=LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@RequestMapping(path={"/msg/list"},method = {RequestMethod.GET})
	public String conversationDetail(Model model){
		try {
			List<ViewObject> conversations = new ArrayList<>();
			int localUserId = hostHolder.getUser().getId();
			List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
			for(Message msg:conversationList){
				ViewObject vo = new ViewObject();
				vo.set("conversation", msg);
				int targetId = msg.getFromId()==localUserId?msg.getId():msg.getFromId();//找出对话交互的人
				int unreadCount = messageService.getConversationUnreadCount(localUserId, msg.getConversationId());
				User user =userService.getUser(targetId);
				vo.set("user",user);//对话的目标
				vo.set("unread",unreadCount);//未读对话条目
				conversations.add(vo);
			}
			model.addAttribute("conversations", conversations);
			
		} catch (Exception e) {
			logger.error("获取站内信列表失败" + e.getMessage());
		}
		return "letter";
	}	
	/**
	 * 消息中心
	 * @param model
	 * @param cid
	 * @return
	 */
	@RequestMapping("/msg/detail")
	public String conversationDetail(Model model,@RequestParam("conversationId") String cid){
		try {
			List<Message> messagesList = messageService.getConversationDetail(cid, 0, 10);
			List<ViewObject> messages = new ArrayList<>();
			for(Message msg:messagesList){
				ViewObject vo = new ViewObject();
				vo.set("message", msg);
				User user =userService.getUser(msg.getFromId());
				if(user==null){
					continue;
				}
				vo.set("headUrl", user.getHeadUrl());
				vo.set("userId",user.getId());
				messages.add(vo);
			}
			model.addAttribute("messages",messages);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "letterDetail";
	}
	
	@RequestMapping("/msg/addMessage")
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
			@RequestParam("toId") int toId,
			@RequestParam("content") String content
			){
		Message msg = new Message();
		try{
		msg.setContent(content);
		msg.setCreatedDate(new Date());
		msg.setFromId(fromId);
		msg.setToId(toId);
		msg.setConversationId(fromId < toId ? String.format("%d_%d",fromId,toId):String.format("%d_%d", toId, fromId));
		//存入
		messageService.addMessage(msg);
		}catch(Exception e){
			return ToutiaoUtils.getJSONString(1, "插入评论失败");
		}
            return ToutiaoUtils.getJSONString(msg.getId());
		
	}
}
