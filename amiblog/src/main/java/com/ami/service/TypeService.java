package com.ami.service;


import com.ami.pojo.Type;

import java.util.List;

public interface TypeService {

    Type getTypeByName(String name);

    Long saveType(Type type);

    Type getType(Long id);

    List<Type> listType(int page, int size);

    int updateType(Type type, Long id);

    void deleteType(Long id);

    List<Type> findAll();

    List<Type> findAllOrderBlogCount(int page, int size);


}
