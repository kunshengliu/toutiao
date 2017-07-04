package com.uestc.async.handler;

import java.util.Arrays;
import java.util.List;

import com.uestc.async.EventHandler;
import com.uestc.async.EventModel;
import com.uestc.async.EventType;

public class LikeHandler implements EventHandler{

	@Override
	public void doHandle(EventModel model) {
		System.out.println("liked");
	}

	@Override
	public List<EventType> getSuppeortEventTypes() {
		Arrays.asList(EventType.LIKE);
		return null;
	}

}
