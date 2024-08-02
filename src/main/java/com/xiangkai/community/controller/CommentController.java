package com.xiangkai.community.controller;

import com.xiangkai.community.annotation.LoginRequired;
import com.xiangkai.community.model.entity.Comment;
import com.xiangkai.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @LoginRequired
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String add(@PathVariable("discussPostId") Integer discussPostId, Comment comment) {
        commentService.addComment(discussPostId, comment);
        return ("redirect:/post/detail/" + discussPostId);
    }

}
