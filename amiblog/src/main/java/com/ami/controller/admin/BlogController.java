package com.ami.controller.admin;


import com.github.pagehelper.PageInfo;
import com.ami.pojo.Blog;
import com.ami.pojo.Tag;
import com.ami.pojo.Type;
import com.ami.pojo.User;
import com.ami.service.Blogservice;
import com.ami.service.TagService;
import com.ami.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "amin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private Blogservice blogservice;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;


    //博客列表
    @RequestMapping("/blogs")
    public String blogs(@RequestParam(name = "page", required = true, defaultValue = "1") int page,
                        @RequestParam(name = "size", required = true, defaultValue = "5") int size,
                        Model model){
        List<Type> types = typeService.findAll();
        List<Blog> blogs = blogservice.findAllBlog(page, size);
        PageInfo pageInfo = new PageInfo<Blog>(blogs);
        model.addAttribute("types",types);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/blogs";
    }
    //搜索博客
    @RequestMapping("/blogs/search")
    public String search(@RequestParam(name = "page", required = true, defaultValue = "1") int page,
                        @RequestParam(name = "size", required = true, defaultValue = "5") int size,
                       Long typeId, String title,
                        Model model){
        List<Blog> blogs = null;
       if(typeId != null){
           if(!StringUtils.isEmpty(title)){
               blogs = blogservice.findAllBlog(page,size,typeId,title);
           }else{
               blogs = blogservice.findALlBlog(page,size,typeId);
           }
       }else if(!StringUtils.isEmpty(title)){
           blogs = blogservice.findALlBlog(page,size,title);
       }else {
           blogs = blogservice.findAllBlog(page,size);
       }

        PageInfo pageInfo = new PageInfo<Blog>(blogs);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/blogs :: blogList";
    }
    //跳转博客新增页面
    @GetMapping("/blogs/input")
    public String input(Model model){
        List<Tag> tags = tagService.findALl();
        model.addAttribute("tags",tags);
        List<Type> types = typeService.findAll();
        model.addAttribute("types",types);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    //跳转编辑修改文章
    @GetMapping("/blogs/{id}/input")
    public String editinput(@PathVariable Long id, Model model){
        List<Tag> tags = tagService.findALl();
        model.addAttribute("tags",tags);
        List<Type> types = typeService.findAll();
        model.addAttribute("types",types);

        String tagIds = tagService.findTagIdsByBlogId(id);
        Blog blog = blogservice.getBlog(id);
        blog.setTagIds(tagIds);
        model.addAttribute("blog",blog);
        return INPUT;
    }

    //更新博客
    @PostMapping("/blogs/{id}")
    public String editPost(@PathVariable Long id,Blog blog,RedirectAttributes attributes){
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTypeId(blog.getType().getId());
        blog.setTags(tagService.findAllByIds(blog.getTagIds()));
        int i = blogservice.updateBlog(blog,id);
        if(i == 0){
            attributes.addFlashAttribute("message","操作失败");
        }{
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;
    }

    //新增博客
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        User user = (User) session.getAttribute("user");
        blog.setUser(user);
        blog.setUserId(user.getId());
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTypeId(blog.getType().getId());

        String tagIds = blog.getTagIds();

        blog.setTags(tagService.findAllByIds(blog.getTagIds()));
        Long i = blogservice.saveBlog(blog);
        if(i == 0){
            attributes.addFlashAttribute("message","操作失败");
        }{
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;

    }
    //删除博客
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogservice.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }
}
