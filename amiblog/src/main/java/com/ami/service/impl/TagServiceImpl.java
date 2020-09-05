package com.ami.service.impl;

import com.ami.dao.BlogTagDao;
import com.ami.dao.TagDao;
import com.ami.pojo.Tag;
import com.ami.service.TagService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private BlogTagDao blogTagDao;

    @Transactional
    @Override
    public Long saveTag(Tag tag) {
        tagDao.saveTag(tag);
        Long i = tag.getId();
        return i;
    }

    @Override
    public Tag getTagByName(String name) {
        Tag tag = tagDao.getTagByName(name);
        return tag;
    }

    @Override
    public Tag getTag(Long id) {
        Tag tag = tagDao.getTag(id);
        return tag;
    }

    @Override
    public List<Tag> listTag(int page,int size) {
        PageHelper.startPage(page,size);
        List<Tag> tags = tagDao.listTag();
        return tags;
    }

    @Transactional
    @Override
    public int updateTag(Tag tag,Long id) {
        int i = tagDao.updateTag(tag.getName(),id);
        return i;
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        //删除标签先将中间表删除
        blogTagDao.deleteByTagId(id);
        tagDao.deleteTag(id);
    }

    @Override
    public List<Tag> findALl() {
        List<Tag> tags = tagDao.findAll();
        return tags;
    }

    @Override
    public List<Tag> findAllByIds(String ids) {
        List<Tag> tags = new ArrayList<>();
        if(!StringUtils.isEmpty(ids)){
            String[] strings = ids.split(",");
            for (String str:strings){
                Long id = Long.valueOf(str);
                Tag tag = tagDao.getTag(id);
                tags.add(tag);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> findAllByBlogId(Long blogId) {
        List<Tag> tags = tagDao.findAllByBlogId(blogId);
        return tags;
    }

    @Override
    public String findTagIdsByBlogId(Long blogId) {
        List<Long> ids = blogTagDao.findTagIdsByBlogId(blogId);
        StringBuilder sb = new StringBuilder();
        if(!CollectionUtils.isEmpty(ids)){
            for (int i = 0; i <ids.size() ; i++) {
                if(i == ids.size()-1){
                    sb.append(String.valueOf(ids.get(i)));
                }else {
                    sb.append(String.valueOf(ids.get(i))).append(",");
                }
            }
        }
        String tagIds = sb.toString();
        if("".equals(tagIds)){
            return null;
        }
        return tagIds;
    }

    @Override
    public List<Tag> listTagOrderByCountBlogs(int page,int size) {
        PageHelper.startPage(page,size);
        List<Tag> tags = tagDao.listTagOrderByCountBlogs();
        return tags;
    }
}
