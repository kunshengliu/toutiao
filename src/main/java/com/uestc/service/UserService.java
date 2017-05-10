package com.uestc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.dao.UserDAO;
import com.uestc.model.User;
@Service
public class UserService {
	@Autowired
	UserDAO userDAO;
	
	public User getUser (int id){
		return userDAO.selectById(id);
	}
}
