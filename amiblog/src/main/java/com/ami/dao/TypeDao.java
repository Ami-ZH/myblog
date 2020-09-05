package com.ami.dao;


import com.ami.pojo.Type;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TypeDao {

    //保存
    @Insert("insert into t_type(id,name) values(null,#{name})")
    @SelectKey(keyColumn="id",keyProperty="id",resultType=Long.class,
            before = false, statement = { "select last_insert_id()" })
    Long saveType(Type type);

    //根据id查询一个
    @Select("select * from t_type where id = #{id}")
    Type getType(Long id);

    //根据名称来查询
    @Select("select * from t_type where name = #{name}")
    Type getTypeByName(String name);

    //查询所有
    @Select("select * from t_type")
    @Results(id = "typeMap",value={
            @Result(id=true,column="id",property="id"),
            @Result(column="name",property="name"),
            @Result(column = "id",property = "blogs",many = @Many(select = "com.ami.dao.BlogDao.findBlogByTypeId",fetchType = FetchType.LAZY))
    })
    List<Type> listType();

    //更新
    @Update("update t_type set name=#{name} where id = #{id}")
    int updateType(@Param("name") String name, @Param("id") Long id);

    //根据id删除
    @Delete("delete from t_type where id = #{id}")
    void deleteType(Long id);

    @Select("select * from t_type")
    List<Type> findAll();

    @Select("SELECT t.* , b.count FROM t_type t LEFT JOIN(SELECT type_id,COUNT(*) COUNT FROM t_blog GROUP BY type_id) b ON t.id = b.type_id ORDER BY COUNT DESC")
    @Results(id = "typeCount",value = {
            @Result(id = true,column = "id",property = "id"),
            @Result(id = true,column = "name",property = "name"),
            @Result(id = true,column = "count",property = "count")
    })
    List<Type> findAllOrderBlogCount();
}
