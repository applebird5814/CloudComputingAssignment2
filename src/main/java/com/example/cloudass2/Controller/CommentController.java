package com.example.cloudass2.Controller;

import com.example.cloudass2.Entity.Comment;

import com.example.cloudass2.Entity.Response;
import com.example.cloudass2.service.CommentService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    @Qualifier("commentServiceImpl")
    CommentService commentService;

    @ResponseBody
    @RequestMapping("addComment")
    private String addComment(@RequestBody @Valid Comment comment, BindingResult result){
        System.out.println(comment);
        commentService.addComment(comment);
        return new Gson().toJson(new Response(true,"评论添加成功！"));
    }

}
