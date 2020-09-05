package com.ami.service;

import com.ami.pojo.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findCommentsByblogId(Long blogId);

    Long saveComment(Comment comment);

    Long deleteComment(Long id);
}
