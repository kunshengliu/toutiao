package com.uestc.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uestc.async.EventHandler;
import com.uestc.async.EventModel;
import com.uestc.async.EventType;
import com.uestc.model.Message;
import com.uestc.model.User;
import com.uestc.service.MessageService;
import com.uestc.service.UserService;
@Component
public class CommentHandler implements EventHandler {
	@Autowired
	private UserService userService;
	@Autowired
	private MessageService messageService;
	
	@Override
	public void doHandle(EventModel model) {
		System.out.println("处理评论");
		Message message = new Message();
		message.setFromId(model.getActorId());
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		User user = userService.getUser(message.getFromId());
		message.setContent("用户"+user.getName()+"评论了你分享的新闻");
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSuppeortEventTypes() {
		return Arrays.asList(EventType.COMMENT);
	}

}
