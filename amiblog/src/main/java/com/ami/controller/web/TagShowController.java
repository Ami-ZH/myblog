package com.ami.controller.web;

import com.github.pagehelper.PageInfo;
import com.ami.pojo.Blog;
import com.ami.pojo.Tag;
import com.ami.service.Blogservice;
import com.ami.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private Blogservice blogService;

    @GetMapping("/tags/{id}")
    public String tags(@RequestParam(name = "page", required = true, defaultValue = "1") int page,
                       @RequestParam(name = "size", required = true, defaultValue = "5") int size,
                       @PathVariable Long id, Model model){
//        List<Tag> tags = tagService.listTag(1, 1000);
        List<Tag> tags = tagService.listTagOrderByCountBlogs(1, 1000);
        PageInfo<Tag> tagPageInfo = new PageInfo<Tag>(tags);
        if(id == -1L){//说明是从导航点击标签进来的
           id = tags.get(0).getId();
        }
        List<Blog> blogs = blogService.findBlogByTagId(page, 4, id);
        PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
        model.addAttribute("tags",tagPageInfo);
        model.addAttribute("page",blogPageInfo);
        model.addAttribute("activeTypeId",id);
        return "tags";
    }
}
