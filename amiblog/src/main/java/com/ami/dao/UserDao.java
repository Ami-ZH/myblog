package com.ami.dao;

import com.ami.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {

    @Select("select * from t_user where username = #{username} and password = #{password}")
    User findUser(@Param("username") String username, @Param("password") String password);

    @Select("select * from t_user where id = #{id}")
    User getUserById(Long id);

}
