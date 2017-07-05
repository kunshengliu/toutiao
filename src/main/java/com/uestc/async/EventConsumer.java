package com.uestc.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.uestc.util.JedisAdapter;
import com.uestc.util.RedisKeyUtil;
/**
 * 事件消费者
 *
 * @author liukunsheng
 *
 */
@Component
public class EventConsumer implements InitializingBean,ApplicationContextAware{
	private static final Logger logger =LoggerFactory.getLogger(EventConsumer.class);
	
	@Autowired
	private JedisAdapter jedisAdapter;
	
	private Map<EventType,List<EventHandler>> config = new HashMap<EventType,List<EventHandler>>();
	
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String,EventHandler> beans  =applicationContext.getBeansOfType(EventHandler.class);
		if(beans!=null){
			for(Entry<String,EventHandler> entry:beans.entrySet()){
				List<EventType> eventTypes= entry.getValue().getSuppeortEventTypes();
				for(EventType type:eventTypes){
					if(!config.containsKey(type)){
						//System.out.println(+type.getValue());
						config.put(type, new ArrayList<EventHandler>());
					}
					config.get(type).add(entry.getValue());
				}
			}
		}
		//开启线程
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					String key = RedisKeyUtil.getEventQueueKey();
					List<String> events =jedisAdapter.brpop(0, key);
					for(String message:events){
						if(message.equals(key)){
							continue;
						}
						System.out.println("message"+message);
						EventModel eventModel = JSON.parseObject(message,EventModel.class);
						if(!config.containsKey(eventModel.getType())){
							logger.error("error,不存在这个类型的东西啊");
							continue;
						}
						for(EventHandler handler :config.get(eventModel.getType())){
							handler.doHandle(eventModel);
						}
					}
				}
			}
		});
		thread.start();//开启消费线程
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext=arg0;
	}

}
