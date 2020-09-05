package com.ami.service.impl;

import com.github.pagehelper.PageHelper;
import com.ami.dao.BlogDao;
import com.ami.dao.BlogTagDao;
import com.ami.dao.CommentDao;
import com.ami.dao.TagDao;
import com.ami.pojo.Blog;
import com.ami.service.Blogservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements Blogservice {

    @Autowired
    private BlogDao blogDao;
    @Autowired
    private BlogTagDao blogTagDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private CommentDao commentDao;

    @Override
    public Blog getBlog(Long id) {
        Blog blog = blogDao.getBlog(id);
        //访问一次那么对应的博客的浏览次数+1
        Integer views = blog.getViews();
        views +=1;
        blogDao.updateBlogViews(views,id);
        return blog;
    }

    @Override
    public List<Blog> listBlog(int page, int size) {
        PageHelper.startPage(page, size);
        List<Blog> blogs = blogDao.listBlog();
        return blogs;
    }

    @Override
    public List<Blog> listBlogByTitle(int page, int size, String title) {
        String orderBy = "update_time desc";
        PageHelper.startPage(page, size, orderBy);
        List<Blog> blogs = blogDao.listBlogByTitle("%" + title + "%");
        return blogs;
    }

    @Override
    public List<Blog> listBlogByTypeId(int page, int size, Long id) {
        PageHelper.startPage(page, size);
        List<Blog> blogs = blogDao.listBlogByTypeId(id);
        return blogs;
    }

    @Override
    public List<Blog> listBlogByTypeIdAndTitle(int page, int size, String title, Long id) {
        String orderBy = "update_time desc";
        PageHelper.startPage(page, size, orderBy);
        List<Blog> blogs = blogDao.listBlogByTypeIdAndTitle(id, "%" + title + "%");
        return blogs;
    }


    @Override
    public List<Blog> findAllBlog(int page, int size) {
        String orderBy = "update_time desc";
        PageHelper.startPage(page, size, orderBy);
        List<Blog> blogs = blogDao.findAllBlog();
        return blogs;
    }

    @Override
    public List<Blog> findAllBlog(int page, int size, Long id, String title) {
        String orderBy = "update_time desc";
        PageHelper.startPage(page, size, orderBy);
        List<Blog> blogs = blogDao.findAllBlogByIdAndTitle(id, "%" + title + "%");
        return blogs;
    }


    @Override
    public List<Blog> findALlBlog(int page, int size, Long id) {
        String orderBy = "update_time desc";
        PageHelper.startPage(page, size, orderBy);
        List<Blog> blogs = blogDao.findALlBlogById(id);
        return blogs;
    }


    @Override
    public List<Blog> findALlBlog(int page, int size, String title) {
        String orderBy = "update_time desc";
        PageHelper.startPage(page, size, orderBy);
        List<Blog> blogs = blogDao.findALlBlogByTitle("%" + title + "%");
        return blogs;
    }

    @Transactional
    @Override
    public Long saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        blogDao.saveBlog(blog);
        Long blogId = blog.getId();
        //操作中间表blog_tag
        String tagIds = blog.getTagIds();
        blogTagDao.delete(blogId);
        if (!StringUtils.isEmpty(tagIds)) {
            String[] strings = tagIds.split(",");
            for (String str : strings) {
                Long tagId = Long.valueOf(str);
                blogTagDao.save(blogId, tagId);
            }
        }

        return blogId;
    }

    @Transactional
    @Override
    public int updateBlog(Blog blog, Long id) {
        Date updateTime = new Date();
        blog.setUpdateTime(updateTime);
        int i = blogDao.updateBlog(blog, id);


        //操作中间表blog_tag与t_tag
        String tagIds = blog.getTagIds();
        blogTagDao.delete(id);
        if (!StringUtils.isEmpty(tagIds)) {
            String[] strings = tagIds.split(",");
            for (String str : strings) {
                Long tagId = Long.valueOf(str);
                blogTagDao.save(id, tagId);
            }
        }

        return i;
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        //删除中间表blog_tag
        blogTagDao.delete(id);
        //删除评论t_comment
        commentDao.deleteByBlogId(id);
        blogDao.deleteBlog(id);
    }

    @Override
    public List<Blog> findNewBlogs(int page, int size) {
        PageHelper.startPage(page,size);
        List<Blog> newBlogs = blogDao.findNewBlogs();
        return newBlogs;
    }

    @Override
    public List<Blog> searchBlogs(int page,int size,String query) {
        PageHelper.startPage(page,size);
        List<Blog> blogs = blogDao.searchBlogs("%" + query + "%");
        return blogs;
    }

    @Override
    public void updateBlogViews(Integer views,Long id) {
        blogDao.updateBlogViews(views,id);
    }


    @Override
    public List<Blog> findBlogByTagId(int page, int size, Long id) {
        PageHelper.startPage(page,size);
        List<Blog> blogs = blogDao.findBlogByTagId(id);
        return blogs;
    }


    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findYearGroup();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for(String yearstr : years){
            List<Blog> blogList = blogDao.findBlogGroupByYear(yearstr);
            map.put(yearstr,blogList);
        }

        return map;
    }

    @Override
    public Long countBlog() {
        Long count = blogDao.countBlog();
        return count;
    }

    @Override
    public Long sumViews() {
        Long sum = blogDao.sumViews();
        return sum;

    }

    @Override
    public Blog getBlogByTypeId(Long id) {
        Blog blog = blogDao.getBlogByTypeId(id);
        return blog;
    }
}
