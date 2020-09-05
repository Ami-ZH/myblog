package com.ami.service;


import com.ami.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface Blogservice {

    Blog getBlog(Long id);

    List<Blog> listBlog(int page, int size);

    List<Blog> listBlogByTitle(int page, int size, String title);

    List<Blog> listBlogByTypeId(int page, int size, Long id);

    List<Blog> findBlogByTagId(int page, int size, Long id);

    List<Blog> listBlogByTypeIdAndTitle(int page, int size, String title, Long id);


    List<Blog> findAllBlog(int page, int size);


    List<Blog> findAllBlog(int page, int size, Long id, String title);


    List<Blog> findALlBlog(int page, int size, Long id);


    List<Blog> findALlBlog(int page, int size, String title);

    Long saveBlog(Blog blog);

    int updateBlog(Blog blog, Long id);

    void deleteBlog(Long id);

    List<Blog> findNewBlogs(int page, int size);

    List<Blog> searchBlogs(int page, int size, String query);

    void updateBlogViews(Integer views, Long id);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Long sumViews();

    Blog getBlogByTypeId(Long id);
}
