package com.uestc.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.model.Message;
import com.uestc.service.MessageService;
import com.uestc.util.ToutiaoUtils;

@Controller
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@RequestMapping("/msg/detail")
	public String conversationDetail(){
		
		return null;
	}
	
	
	@RequestMapping("/msg/addMessage")
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
			@RequestParam("toId") int toId,
			@RequestParam("content") String content
			){
		Message msg = new Message();
		msg.setContent(content);
		msg.setCreatedDate(new Date());
		msg.setFromId(fromId);
		msg.setToId(toId);
		msg.setConversationId(fromId < toId ? String.format("%d_%d",fromId,toId):String.format("%d_%d", toId, fromId));
		//存入
		messageService.addMessage(msg);
            return ToutiaoUtils.getJSONString(msg.getId());
		
	}
}
