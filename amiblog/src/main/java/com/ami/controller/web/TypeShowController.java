package com.ami.controller.web;

import com.ami.pojo.Blog;
import com.ami.pojo.Type;
import com.github.pagehelper.PageInfo;
import com.ami.service.Blogservice;
import com.ami.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private Blogservice blogservice;

    @GetMapping("types/{id}")
    public String types(@RequestParam(name = "page", required = true, defaultValue = "1") int page,
                        @RequestParam(name = "size", required = true, defaultValue = "5") int size,
                        @PathVariable Long id, Model model){

//        List<Type> types = typeService.listType(1,1000);
        //由于分类过少，因此不进行分页展示，分页的话，第二页可能只有一个分类。一般在每页都要求显示完整的分类
        List<Type> types = typeService.findAllOrderBlogCount(1, 1000);
        PageInfo<Type> typePageInfo = new PageInfo<Type>(types);
        if(id == -1L){
            id = types.get(0).getId();
        }
        List<Blog> blogs = blogservice.listBlogByTypeId(page, 4, id);
        PageInfo<Blog> blogPageInfo = new PageInfo<Blog>(blogs);
        model.addAttribute("types",typePageInfo);
        model.addAttribute("page",blogPageInfo);
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
