package com.uestc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.dao.MessageDAO;
import com.uestc.model.Message;

@Service
public class MessageService {
	@Autowired
	private MessageDAO messageDAO;
	/**
	 * 站内信的添加
	 * @param message
	 * @return
	 */
	public int addMessage(Message message){
		return messageDAO.addMessage(message);
	}
	/**
	 * 根据实验跑
	 * @param cid
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Message> getConpversationList(int cid,int offset,int limit){
		return messageDAO.getConversationDetail(cid, offset, limit);
	}
	
	
	
	
}
