package com.ami.service;

import com.ami.pojo.Tag;

import java.util.List;

public interface TagService {

    Tag getTagByName(String name);

    Long saveTag(Tag tag);

    Tag getTag(Long id);

    List<Tag> listTag(int page, int size);

    int updateTag(Tag tag, Long id);

    void deleteTag(Long id);

    List<Tag> findALl();

    List<Tag> findAllByIds(String ids);

    List<Tag> findAllByBlogId(Long blogId);

    String findTagIdsByBlogId(Long blogId);

    List<Tag> listTagOrderByCountBlogs(int page, int size);


}
