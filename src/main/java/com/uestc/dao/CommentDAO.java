package com.uestc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.uestc.model.Comment;

@Mapper
public interface CommentDAO {
	String TABLE_NAME = "comment";
	String INSERT_FIELDS = "content,user_id,entity_id,entity_type,created_date,status";
	String SELECT_FILEDS = "id,"+INSERT_FIELDS;
	
	@Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIELDS,") values ( "
			+ " #{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status} )"})
	int addComment(Comment comment);
	
	@Select({"select ",SELECT_FILEDS," from ",TABLE_NAME," "
			+ "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
	List<Comment> selectByEntity(@Param("entityId") int entityId ,@Param("entityType") int entityType);
	
	
	@Select({"select count(*) from ",TABLE_NAME," "
			+ "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
	int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);
	
	
	
}
 