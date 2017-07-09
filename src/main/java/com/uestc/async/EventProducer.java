package com.uestc.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uestc.util.JedisAdapter;
import com.uestc.util.RedisKeyUtil;
/**
 * 生产者，push到redis中
 * @author liukunsheng
 *
 */
@Service
public class EventProducer {
	private static Logger logger = LoggerFactory.getLogger(EventProducer.class);
	
	@Autowired
	private JedisAdapter  jedisAdapter;
	/**
	 * 插入时间到redis这个消息队列中
	 * @param model
	 * @return
	 */
	public boolean fireEvent(EventModel model){
		try {
			String json =JSONObject.toJSONString(model);
			String key = RedisKeyUtil.getEventQueueKey();
			System.out.println("fireEvent:"+key+"  "+json);
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			logger.error("消息队列有错 "+e.getMessage());
			return false;
		}
	}
}
