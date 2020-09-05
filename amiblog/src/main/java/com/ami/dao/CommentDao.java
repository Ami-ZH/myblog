package com.ami.dao;

import com.ami.pojo.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface CommentDao {

    @Select("select * from t_comment where blog_id = #{blogId} order by create_time desc")
    @Results(id = "commentMap",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "nickname",property = "nickname"),
            @Result(column = "email",property = "email"),
            @Result(column = "content",property = "content"),
            @Result(column = "avatar",property = "avatar"),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "blog_id",property = "blogId"),
            @Result(column = "admin_comment",property = "adminComment"),
            @Result(column = "parent_comment_id",property = "parentCommentId")
    })
    List<Comment> findCommentsByblogId(Long blogId);

    @Select("select * from t_comment where blog_id = #{blogId} and parent_comment_id = #{Pid} order by create_time desc")
    @ResultMap("commentMap")
    List<Comment> findCommentsByBlogIdAndPid(@Param("blogId") Long blogId, @Param("Pid") Long Pid);

    @Insert("insert into t_comment(nickname,email,content,avatar,create_time,blog_id,parent_comment_id,admin_comment) values(#{nickname},#{email},#{content},#{avatar},#{createTime},#{blogId},#{parentCommentId},#{adminComment})")
    @SelectKey(keyProperty = "id", keyColumn = "id",
            before = false,statement = { "select last_insert_id()" },
            resultType = Long.class)
    Long saveComment(Comment comment);

    @Select("select * from t_comment where parent_comment_id = #{parentCommentId}")
    @ResultMap("commentMap")
    Comment findOneByParentCommentId(Long parentCommentId);

    @Delete("delete from t_comment where blog_id = #{blogId}")
    void deleteByBlogId(long blogId);

    @Delete("delete from t_comment where id = #{id}")
    void deleteCommentByCommentId(Long id);
    @Select("select blog_id from t_comment where id = #{id}")
    Long selectBlogIdByCommentId(Long id);
}
