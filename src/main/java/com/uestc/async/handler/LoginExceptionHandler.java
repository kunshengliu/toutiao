package com.uestc.async.handler;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uestc.async.EventHandler;
import com.uestc.async.EventModel;
import com.uestc.async.EventType;
import com.uestc.service.MessageService;

@Component
public class LoginExceptionHandler implements EventHandler{
	@Autowired
	private MessageService messageService;
	
	@Override
	public void doHandle(EventModel model) {
		//判断异常登陆
		
		
	}

	@Override
	public List<EventType> getSuppeortEventTypes() {
		return Arrays.asList(EventType.LOGIN);
	}

}
