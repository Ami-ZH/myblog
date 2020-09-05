package com.ami.controller.web;


import com.github.pagehelper.PageInfo;
import com.ami.pojo.Blog;
import com.ami.pojo.Tag;
import com.ami.pojo.Type;
import com.ami.service.Blogservice;
import com.ami.service.TagService;
import com.ami.service.TypeService;
import com.ami.uitls.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private Blogservice blogservice;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@RequestParam(name = "page", required = true, defaultValue = "1") int page,
                        @RequestParam(name = "size", required = true, defaultValue = "5") int size,
                        Model model){
        //1:查询4条博客
        List<Blog> blogs = blogservice.findAllBlog(page, 4);
        PageInfo<Blog> pageInfoBlog = new PageInfo<Blog>(blogs);
        model.addAttribute("page",pageInfoBlog);
        //2:查询5个类型
//        List<Type> types = typeService.listType(page, size);
        List<Type> types = typeService.findAllOrderBlogCount(page, size);
        PageInfo<Type> pageInfoType = new PageInfo<Type>(types);
        model.addAttribute("types",pageInfoType);
        //3:查询5个标签
//        List<Tag> tags = tagService.listTag(page, size);
        List<Tag> tags = tagService.listTagOrderByCountBlogs(page, size);
        PageInfo<Tag> pageInfoTag = new PageInfo<Tag>(tags);
        model.addAttribute("tags",pageInfoTag);
        //4:查询5条推荐的最新的博客
        List<Blog> newBlogs = blogservice.findNewBlogs(page, size);
        PageInfo<Blog> pageInfoNewBlogs = new PageInfo<Blog>(newBlogs);
        model.addAttribute("recommendBlogs",pageInfoNewBlogs);
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        Blog blog = blogservice.getBlog(id);
        if(blog == null){
            throw new RuntimeException("该博客不存在");
        }
        String content = blog.getContent();
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        model.addAttribute("blog",blog);
        return "blog";
    }

    @PostMapping("/search")
    public String search(@RequestParam(name = "page", required = true, defaultValue = "1") int page,
                         @RequestParam(name = "size", required = true, defaultValue = "5") int size,
                         @RequestParam String query,
                         Model model){
        List<Blog> blogs = blogservice.searchBlogs(page,size,query);
        if(CollectionUtils.isEmpty(blogs)){
            throw new RuntimeException("不存在该博客");
        }
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("page",pageInfo);
        model.addAttribute("query",query);
        return "search";

    }
}
