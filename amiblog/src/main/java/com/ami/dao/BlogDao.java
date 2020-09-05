package com.ami.dao;

import com.ami.pojo.Blog;
import com.ami.pojo.Type;
import com.ami.pojo.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogDao {

    @Select("select * from t_blog where id = #{id}")
    @ResultMap("blogsMap")
    Blog getBlog(Long id);

    @Select("select * from t_blog where type_id = #{id}")
    @ResultMap("blogsMap")
    Blog getBlogByTypeId(Long id);

    //查询所有blog,全字段封装
    @Select("select * from t_blog")
    @Results(id = "blogMap",value = {
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "title",property = "title"),
            @Result(column = "content",property = "content"),
            @Result(column = "first_picture",property = "firstPicture"),
            @Result(column = "flag",property = "flag"),
            @Result(column = "views",property = "views"),
            @Result(column = "appreciation",property = "appreciation"),
            @Result(column = "share_statment",property = "shareStatment"),
            @Result(column = "commentabled",property = "commentabled"),
            @Result(column = "published",property = "published"),
            @Result(column = "recommend",property = "recommend"),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "type_id",property = "typeId"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "user_id",property = "user",one = @One(select="com.ami.dao.UserDao.getUserById",fetchType = FetchType.LAZY)),
            @Result(column = "type_id",property = "type",one = @One(select="com.ami.dao.TypeDao.getType", fetchType= FetchType.LAZY)),
            @Result(column = "id",property = "comments",many = @Many(select = "com.ami.dao.CommentDao.listCommentByIds",fetchType = FetchType.LAZY)),
            @Result(column = "id",property = "tags",many = @Many(select = "com.ami.dao.TagDao.listTagByIds",fetchType = FetchType.LAZY)),

    })
    List<Blog> listBlog();

    @Select("select * from t_blog where title like #{title}")
    List<Blog> listBlogByTitle(String title);

    @Select("select * from t_blog where type_id = #{id} order by update_time desc")
    @ResultMap("blogsMap")
    List<Blog> listBlogByTypeId(Long id);

    @Select("select * from t_blog where type_id = #{id} and title like #{title}")
    @ResultMap("blogMap")
    List<Blog> listBlogByTypeIdAndTitle(@Param("id") Long id, @Param("title") String title);

    //查询所有blog，部分字段封装
    @Select("select * from t_blog")
    @Results(id = "blogsMap",value={
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "title",property = "title"),
            @Result(column = "recommend",property = "recommend"),
            @Result(column = "views",property = "views"),
            @Result(column = "description",property = "description"),
            @Result(column = "first_picture",property = "firstPicture"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "type_id",property = "type",javaType = Type.class,one = @One(select="com.ami.dao.TypeDao.getType", fetchType= FetchType.LAZY)),
            @Result(column = "user_id",property = "user",javaType = User.class,one = @One(select="com.ami.dao.UserDao.getUserById", fetchType= FetchType.LAZY)),
            @Result(column = "id",property = "tags",many = @Many(select = "com.ami.dao.TagDao.findAllByBlogId",fetchType = FetchType.LAZY)),
    })
    List<Blog> findAllBlog();


    @Select("select * from t_blog where type_id = #{id} and title like #{title}")
    @ResultMap("blogsMap")
    List<Blog> findAllBlogByIdAndTitle(@Param("id") Long id, @Param("title") String title);


    @Select("select * from t_blog where type_id = #{id}")
    @ResultMap("blogsMap")
    List<Blog> findALlBlogById(Long id);


    @Select("select * from t_blog where title like #{title}")
    @ResultMap("blogsMap")
    List<Blog> findALlBlogByTitle(String title);

    @Select("select * from t_blog where type_id = #{id}")
    @ResultMap("blogsMap")
    List<Blog> findBlogByTypeId(Long id);

    @Select("select * from t_blog where id in (select blog_id from blog_tag where tag_id = #{id})")
    @ResultMap("blogsMap")
    List<Blog> findBlogByTagId(Long id);


    @Insert("insert into t_blog(id,title,content,first_picture,flag,views,description,appreciation,share_statment,commentabled,published,recommend,create_time,update_time,type_id,user_id) values(null,#{title},#{content},#{firstPicture},#{flag},#{views},#{description},#{appreciation},#{shareStatment},#{commentabled},#{published},#{recommend},#{createTime},#{updateTime},#{typeId},#{userId})")
    @SelectKey(keyColumn="id",keyProperty="id",resultType=Long.class,
            before = false, statement = { "select last_insert_id()" })
    Long saveBlog(Blog blog);

    @Update("update t_blog set title = #{blog.title},content = #{blog.content},first_picture = #{blog.firstPicture},flag = #{blog.flag},description=#{blog.description},appreciation = #{blog.appreciation},share_statment =#{blog.shareStatment},commentabled =#{blog.commentabled},published=#{blog.published},recommend=#{blog.recommend},update_time=#{blog.updateTime},type_id=#{blog.typeId} where id = #{id} " )
    int updateBlog(@Param("blog") Blog blog, @Param("id") Long id);

    @Delete("delete from t_blog where id = #{id}")
    void deleteBlog(Long id);

    @Select("select * from t_blog where recommend = true order by update_time desc")
    List<Blog> findNewBlogs();

    @Select("select * from t_blog where title like #{query} or description like #{query} or content like #{query}")
    @ResultMap("blogsMap")
    List<Blog> searchBlogs(String query);

    @Update("update t_blog set views = #{views} where id = #{id}")
    void updateBlogViews(@Param("views") Integer views, @Param("id") Long id);


    @Select("select date_format(b.update_time,'%Y') as year from t_blog b group by year order by year desc")
    List<String> findYearGroup();

    @Select("select * from t_blog b where date_format(b.update_time,'%Y') =#{yearstr} order by update_time desc")
    @ResultMap("blogsMap")
    List<Blog> findBlogGroupByYear(String yearstr);

    @Select("select count(*) from t_blog")
    Long countBlog();

    @Select("select sum(views) from t_blog ")
    Long sumViews();

}
