package com.uestc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.uestc.model.News;
import com.uestc.model.User;
@Mapper
public interface NewsDAO {
	String TABLE_NAME="news";
	String INSERT_FIELDS="title,link,image,like_count,comment_count,created_date,user_id";
	String SELECT_FIELDS="id,"+INSERT_FIELDS;
	
	
	
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
		") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
	int addNews(News news);
	
	List<News> selectByUserIdAndOffset(@Param("userId")int userId,@Param("offset") int offset,
			                            @Param("limit") int limit);
	
	
}
