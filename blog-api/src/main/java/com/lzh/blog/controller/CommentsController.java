package com.lzh.blog.controller;

import com.lzh.blog.service.CommentsService;
import com.lzh.blog.vo.Result;
import com.lzh.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId){

        return commentsService.commentsByArticleId(articleId);

    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){

        return commentsService.comment(commentParam);
    }
}
