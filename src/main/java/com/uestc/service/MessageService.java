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
	 * 根据会话
	 * @param cid 会话ID
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Message> getConversationDetail(String cid,int offset,int limit){
		return messageDAO.getConversationDetail(cid, offset, limit);
	}
	public List<Message> getConversationList(int userId ,int offset,int limit){
		return messageDAO.getConversationList(userId,offset,limit);
	}
	
	public int getConversationUnreadCount(int userId,String cid){
		return messageDAO.getConversationUnreadCount(userId, cid);
	}
	
	
}
