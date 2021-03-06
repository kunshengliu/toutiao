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
public class LikeHandler implements EventHandler{
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void doHandle(EventModel model) {
		System.out.println("liked");
		Message message = new Message();
		User user  =userService.getUser(model.getActorId());//这是发信息的用户
		message.setFromId(model.getActorId());
		message.setCreatedDate(new Date());
		message.setToId(model.getEntityOwnerId());
		message.setContent("用户"+user.getName()
		+"赞了你的新闻咨询，http://localhost:8080/news/"
		+String.valueOf(model.getEntityId()));
		//message.setConversationId();
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSuppeortEventTypes() {
		return Arrays.asList(EventType.LIKE);
	}
}
