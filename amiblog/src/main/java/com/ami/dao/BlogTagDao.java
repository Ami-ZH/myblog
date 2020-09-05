package com.ami.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogTagDao {
    @Insert("insert into blog_tag(blog_id,tag_id)values(#{blogId},#{tagId})")
    void save(@Param("blogId") Long blogId, @Param("tagId") Long tagId);

    @Select("select tag_id from blog_tag where blog_Id = #{blogId}")
    List<Long> findTagIdsByBlogId(Long blogId);

    @Delete("delete from blog_tag where blog_id =#{blogId}")
    void delete(Long blogId);

    @Delete("delete from blog_tag where tag_id = #{TagId}")
    void deleteByTagId(Long TagId);
}
