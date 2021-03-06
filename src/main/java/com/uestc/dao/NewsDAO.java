package com.uestc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.uestc.model.News;
import com.uestc.model.NewsScore;
import com.uestc.model.User;
@Mapper
public interface NewsDAO {
	String TABLE_NAME="news";
	String INSERT_FIELDS="title,link,image,like_count,comment_count,created_date,date,user_id,score";
	String SELECT_FIELDS="id,"+INSERT_FIELDS;
	
	@Select({"select",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	News selectByNewsId(int id);
	
	
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
		") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{date},#{userId},#{score})"})
	int addNews(News news);
	
	List<News> selectByUserIdAndOffset(@Param("userId")int userId,@Param("offset") int offset,
			                            @Param("limit") int limit);
	
	@Update({"UPDATE ",TABLE_NAME," SET comment_count=#{count} WHERE id=#{newsId}"})
	int updateCommentCount(@Param("newsId") int newsId ,@Param("count") int count);
	
	@Update({"UPDATE ",TABLE_NAME," SET like_count=#{count} WHERE id=#{newsId}"})
	int updateLikeCount(@Param("newsId") int newsId,@Param("count") int count);
	
	int updateScoreBatch(List<NewsScore> list);
	
}
