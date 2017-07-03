package com.uestc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.uestc.model.Message;

/**
 * mybatis 
 * @author liukunsheng
 *
 */
@Mapper
public interface MessageDAO {
	String TABLE_NAME="message";
	String INSERT_FIELDS=" from_id,to_id,content,created_date,has_read,conversation_id ";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;
    /**
     * 插入消息
     * @param message
     * @return
     */
    @Insert({"INSERT INTO ",TABLE_NAME, " (",INSERT_FIELDS,") "
    		+ "VALUES ( #{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationID})" })
    int addMessage(Message message);
	
    /**
     * 通过会话去查找会话
     * @param cid
     * @return
     */
    @Select({"SELECT ",SELECT_FIELDS," FROM ",TABLE_NAME, " where conversation_id=#{cid} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("cid") String cid,@Param("offset") int offset,@Param("limit") int limit );
    
	/**
	 * 用户查看自己的站内信
	 * @return
	 */
    List<Message> getConversationList(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
    
    /**
     * 未读取的站内信条数
     * @param userId
     * @param conversationId
     * @return
     */
    @Select({"SELECT count(id) from ",TABLE_NAME," WHERE has_read=0 and to_id =#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);
    
    
}
