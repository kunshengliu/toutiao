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
    
    @Insert({"INSERT INTO ",TABLE_NAME, " (",INSERT_FIELDS,") "
    		+ "VALUES ( #{fromId},#{toId},#{content},#{createdDate},#{heaRead},#{conversationID})" })
    int addMessage(Message message);
	
    /**
     * 通过会话去查找会话
     * @param cid
     * @return
     */
    @Select({"SELECT ",SELECT_FIELDS," FROM ",TABLE_NAME, " where conversation_id=#{cid} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("cid") int cid,@Param("offset") int offset,@Param("limit") int limit );
    
	
    
    
    
    
    
}
