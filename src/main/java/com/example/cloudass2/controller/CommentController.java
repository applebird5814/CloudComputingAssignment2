package com.example.cloudass2.controller;

import com.example.cloudass2.entity.Comment;

import com.example.cloudass2.entity.Response;
import com.example.cloudass2.entity.User;
import com.example.cloudass2.service.CommentService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    @Qualifier("commentServiceImpl")
    CommentService commentService;

    @ResponseBody
    @RequestMapping("addComment")
    private String addComment(@RequestBody @Valid Comment comment, BindingResult result,HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession!=null) {
            try {
                User user = (User) httpSession.getAttribute("user");
                comment.setAuthorId(user.getId());
                comment.setAuthor(user.getScreenName());
            }catch (Exception e)
            {

            }
        }
        System.out.println(comment);
        commentService.addComment(comment);
        return new Gson().toJson(new Response(true,"Comment add success!"));
    }

}
