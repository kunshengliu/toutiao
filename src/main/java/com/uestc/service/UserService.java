package com.uestc.service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;












import com.uestc.dao.LoginTicketDAO;
import com.uestc.dao.UserDAO;
import com.uestc.model.LoginTicket;
import com.uestc.model.User;
import com.uestc.util.ToutiaoUtils;
@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	
	@Autowired
	private LoginTicketDAO loginTicketDAO; 
	
	public User getUser (int id){
		return userDAO.selectById(id);
	}
	/**
	 * 注册
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String,Object> register(String username,String password,int remberme){
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(username)){
			map.put("msgname","用户名不能为空");
			return map;
		}
		if(StringUtils.isBlank(password)){
			map.put("msgpwd", "用户不能为空");
			return map;
		}
		
		User user = userDAO.selectByName(username);
		if(user!=null){
			map.put("msgname", "用户已经被注册");
			return map;
		}
		user= new User();
		user.setName(username);//创建name
		
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		String head=String.format("http://images.nowcode.com/head/%dt.png", new Random().nextInt(1000));
		user.setHeadUrl(head);
		user.setPassword(ToutiaoUtils.MD5(password+user.getSalt()));
		userDAO.addUser(user);
		System.out.println("abc");
		//下面是登录，让注册后直接登录
		String ticket = addLoginTicket(userDAO.selectByName(username).getId());
		map.put("ticket", ticket);
		return map;
	}
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String,Object> login(String username,String password){
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(username)){
			map.put("msgname","用户名不能为空");
			return map;
		}
		if(StringUtils.isBlank(password)){
			map.put("msgpwd", "用户不能为空");
			return map;
		}
		
		User user = userDAO.selectByName(username);
		if(user==null){
			map.put("msgname", "用户不存在");
			return map;
		}
		if(!user.getPassword().equals(ToutiaoUtils.MD5(password+user.getSalt()))){
			 map.put("msgpwd", "密码不正确");
			 return map;
		}
		//登录的时候下发一个t票
		System.out.println("下发一个ticket");
		String ticket =addLoginTicket(user.getId());
		map.put("ticket",ticket);
		return map;
	}
	/**
	 * 登录时添加ticket
	 * @param userId，用户的id
	 * @return
	 */
	private String addLoginTicket(int userId){
		LoginTicket ticket = new LoginTicket();
		ticket.setUserId(userId);
		Date date = new Date();
		//设置ticket的有效期，当前时间往后延迟
		date.setTime(date.getTime()+1000*3600*24);
		ticket.setExpired(date);
		ticket.setStatus(0);
		ticket.setTicket(UUID.randomUUID().toString());
		System.out.println("bcd");
		loginTicketDAO.addLoginTicket(ticket);
		
		return ticket.getTicket();
		
	}
	public void logout(String ticket){
		loginTicketDAO.updateLoginTicket(ticket, 1);
	}
	
	
	
}
