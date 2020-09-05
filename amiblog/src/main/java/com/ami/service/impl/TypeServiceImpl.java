package com.ami.service.impl;

import com.ami.pojo.Blog;
import com.ami.service.Blogservice;
import com.github.pagehelper.PageHelper;
import com.ami.dao.TypeDao;
import com.ami.pojo.Type;
import com.ami.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;
    @Autowired
    private Blogservice blogservice;

    @Transactional
    @Override
    public Long saveType(Type type) {
        typeDao.saveType(type);
        Long i = type.getId();
        return i;
    }

    @Override
    public Type getTypeByName(String name) {
        Type type = typeDao.getTypeByName(name);
        return type;
    }

    @Override
    public Type getType(Long id) {
        Type type = typeDao.getType(id);
        return type;
    }

    @Override
    public List<Type> listType(int page,int size) {
        PageHelper.startPage(page,size);
        List<Type> types = typeDao.listType();
        return types;
    }

    @Transactional
    @Override
    public int updateType(Type type,Long id) {
        int i = typeDao.updateType(type.getName(),id);
        return i;
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        //删除分类先将blog的Typeid置为空
        Blog blog = blogservice.getBlogByTypeId(id);
        blog.setTypeId(null);
        Long blogId = blog.getId();
        blogservice.updateBlog(blog,blogId);

        typeDao.deleteType(id);
    }


    @Override
    public List<Type> findAll() {
        List<Type> types = typeDao.findAll();
        return types;
    }

    @Override
    public List<Type> findAllOrderBlogCount(int page, int size) {
        PageHelper.startPage(page,size);
        List<Type> types = typeDao.findAllOrderBlogCount();
        return types;
    }
}
