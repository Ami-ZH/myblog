package com.ami.service.impl;

import com.ami.dao.CommentDao;
import com.ami.pojo.Comment;
import com.ami.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;
    //这是一个存放了一级，二级，多级子评论的集合
//    private List<Comment> childCommentList = new ArrayList<Comment>();
    List<Comment> childCommentList = new CopyOnWriteArrayList<Comment>();

    @Override
    public List<Comment> findCommentsByblogId(Long blogId) {
        //首先查询这个博客中的所有的父评论，它的特点是parentCommentId为-1
        List<Comment> comments = commentDao.findCommentsByBlogIdAndPid(blogId,-1L);

            for(int i = 0;i<comments.size();i++){//得到每一条父评论
                Comment comment = comments.get(i);
                //获取父评论的id与别名
                Long id = comment.getId();
                String nickname = comment.getNickname();
                //查询这条父评论的子评论
                findChildComment(blogId,id,nickname);

                //等待上面的子评论递归完，得到childComments集合然后存入父评论对象中
                comment.setReplyComments(childCommentList);

                childCommentList = new ArrayList<>();
            }

        return comments;
    }

    public void findChildComment(Long blogId,Long pid,String nickname){
        List<Comment> chidComments = commentDao.findCommentsByBlogIdAndPid(blogId, pid);
        if(chidComments.size()>0) {
            for (int i = 0; i < chidComments.size(); i++) {//得到每一条子评论
                Comment comment = chidComments.get(i);
                //将子评论存入子评论集合
                childCommentList.add(comment);
                //设置子评论的父别名
                comment.setParentNickname(nickname);
                //获取子评论的id和自身的别名
                Long id = comment.getId();
                String nickname1 = comment.getNickname();
                //再根据这条子评论的id和别名查询它的子评论(二级子评论)
                findChildComment(blogId, id, nickname1);//此处是递归调用，必须留有出口，理论上评论可能永远有子评论，但是实际上这种情况很少发生，所以这个递归最终会结束
            }

        }

    }

    @Transactional
    @Override
    public Long saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        commentDao.saveComment(comment);
        Long id = comment.getId();
        return id;
    }

    @Override
    public Long deleteComment(Long id) {
        Long blogId = commentDao.selectBlogIdByCommentId(id);
        commentDao.deleteCommentByCommentId(id);
        return blogId;
    }
}
