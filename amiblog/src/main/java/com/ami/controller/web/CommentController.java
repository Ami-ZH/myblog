package com.ami.controller.web;


import com.ami.pojo.Comment;
import com.ami.pojo.User;
import com.ami.service.Blogservice;
import com.ami.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private Blogservice blogService;



    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        List<Comment> comments = commentService.findCommentsByblogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    //提交以及回复评论
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        User user = (User) session.getAttribute("user");
        //设置评论的blogId，表示此评论隶属于该博客
        comment.setBlogId(blogId);
        if(user != null){//说明此条评论来自管理员
            //设置管理员专属头像
            comment.setAvatar(user.getAvatar());
            //标记此评论为管理员评论
            comment.setAdminComment(true);
        }else {//说明不是管理员的评论
            //设置一个普通的头像
            comment.setAvatar("/images/avatar.jpg");
        }
        //获取此条评论中的父评论的id
        Long pid = comment.getParentComment().getId();
        if(pid != -1L){//说明这是一条回复
            comment.setParentCommentId(pid);
        }else{//说明这是一条新的父评论
            comment.setParentCommentId(pid);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
    @PostMapping ("/comment/delete")
    public String deleteCommment(Comment comment,Model model){
        Long id = comment.getParentComment().getId();
        Long blogId = commentService.deleteComment(id);
        List<Comment> comments = commentService.findCommentsByblogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }
}
