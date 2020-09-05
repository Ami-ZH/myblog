package com.ami.controller.web;

import com.ami.pojo.Blog;
import com.ami.service.Blogservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ArchivesController {
    @Autowired
    private Blogservice blogservice;

    @GetMapping("/archives")
    public String archives(Model model){
        Map<String, List<Blog>> archiveBlog = blogservice.archiveBlog();
        //博客总数
        Long countBlog = blogservice.countBlog();
        //博客总浏览次数
        Long sumViews = blogservice.sumViews();
        model.addAttribute("archiveMap",archiveBlog);
        model.addAttribute("blogCount",countBlog);
        model.addAttribute("viewsSum",sumViews);
        return "archives";
    }
}
