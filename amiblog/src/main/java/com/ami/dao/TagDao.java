package com.ami.dao;


import com.ami.pojo.Tag;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagDao {

    //保存
    @Insert("insert into t_tag(id,name) values(null,#{name})")
    @SelectKey(keyColumn="id",keyProperty="id",resultType=Long.class,
            before = false, statement = { "select last_insert_id()" })
    Long saveTag(Tag tag);

    //根据id查询一个
    @Select("select * from t_tag where id = #{id}")
    Tag getTag(Long id);

    //根据名称来查询
    @Select("select * from t_tag where name = #{name}")
    Tag getTagByName(String name);

    //查询所有
    @Select("select * from t_tag")
    @Results(id = "typeMap",value={
            @Result(id=true,column="id",property="id"),
            @Result(column="name",property="name"),
            @Result(column = "id",property = "blogs",many = @Many(select = "com.ami.dao.BlogDao.findBlogByTagId",fetchType = FetchType.LAZY))
    })
    List<Tag> listTag();




    @Select("SELECT t.name,t.id,tb.count FROM t_tag t  LEFT JOIN(SELECT tag_id tid, COUNT(*) COUNT FROM blog_tag  GROUP BY tid) tb ON t.id = tb.tid ORDER BY tb.count DESC")
    @Results(id = "tagcount",value = {
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "count",property = "count"),
    })
    List<Tag> listTagOrderByCountBlogs();


    @Select("select * from t_tag where id in (select tagId from blog_tag where id = #{id})")
    @ResultMap("typeMap")
    List<Tag> listTagByIds(Long id);

    //更新
    @Update("update t_tag set name=#{name} where id = #{id}")
    int updateTag(@Param("name") String name, @Param("id") Long id);

    //根据id删除
    @Delete("delete from t_tag where id = #{id}")
    void deleteTag(Long id);

    @Select("select * from t_tag")
    @ResultMap("typeMap")
    List<Tag> findAll();

    @Select("select * from t_tag where id in (select tag_id from blog_tag where blog_id = #{blogId})")
    @ResultMap("typeMap")
    List<Tag> findAllByBlogId(Long blogId);
}
