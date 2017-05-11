package com.uestc.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.uestc.model.LoginTicket;

@Mapper
public interface LoginTicketDAO {
	String TABLE_NAME="login_ticket";
	String INSERT_FIELDS="user_id,ticket,expired,status";
	String SELECT_FIELDS="id,"+INSERT_FIELDS;
	
	@Insert({"INSERT INTO ",TABLE_NAME,"(",INSERT_FIELDS,") VALUES ("
			+ "#{userId},#{ticket},#{expired},#{status}"})
	int addLoginTicket(LoginTicket ticket);
	
	@Select({"SELECT",SELECT_FIELDS," FROM ",TABLE_NAME," WHERE ticket=#{ticket}"})
	LoginTicket getLonginTicketByTicket(String ticket);
	
	@Update({"UPDATE ",TABLE_NAME, "SET status=#{status} WHERE ticket=#{ticket}"})
	void updateLoginTicket(@Param("ticket") String ticket,@Param("status") int status);
	
	
}
