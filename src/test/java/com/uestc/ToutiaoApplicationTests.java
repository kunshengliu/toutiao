package com.uestc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.uestc.dao.NewsDAO;
import com.uestc.model.NewsScore;
import com.uestc.util.ScheduleWorker;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@WebAppConfiguration
public class ToutiaoApplicationTests {
	@Autowired
	private NewsDAO newsDAO;
	
	@Autowired
	private ScheduleWorker scheduleWorker;
	
	
	@Test
	public void contextLoads() {
		
		scheduleWorker.updateNewsScore();
		
		
	}

}
