package com.example.cloudass2.Controller;

import com.example.cloudass2.Entity.Article;
import com.example.cloudass2.Entity.Response;
import com.example.cloudass2.Entity.User;
import com.example.cloudass2.service.ArticleService;
import com.example.cloudass2.service.CommentService;
import com.example.cloudass2.service.TypeService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    @Qualifier("articleServiceImpl")
    ArticleService articleService;

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeService typeService;

    @Autowired
    @Qualifier("commentServiceImpl")
    CommentService commentService;

    @RequestMapping("/blog")
    public String noBlogId(){
        return "error/ArticleNotFound";
    }

    @RequestMapping("/blog/{id}")
    public String blog(@PathVariable(value = "id") String id,Model model,HttpServletRequest httpServletRequest){
        Article article = articleService.findArticleById(id);
        if(article==null)
        {
            return "error/ArticleNotFound";
        }
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession!=null) {
            try {
                User user = (User) httpSession.getAttribute("user");
                model.addAttribute("ScreenName", new Gson().toJson(user.getScreenName()));
            }catch (Exception e)
            {

            }
        }
        model.addAttribute("Article",new Gson().toJson(article));
        model.addAttribute("Comments",new Gson().toJson(commentService.findCommentsByArticleId(id)));
        return "blog";
    }

    @RequestMapping("/blogList")
    public String blogList(Model model){
        model.addAttribute("Articles",new Gson().toJson(articleService.findAll()));
        return "blogList";
    }

    @RequestMapping("/blogList/{id}")
    public String blogList(Model model, @PathVariable String id){
        model.addAttribute("Articles",new Gson().toJson(articleService.findByType(id)));
        return "blogList";
    }

    @RequestMapping("/editBlog/{id}")
    public String editBlog(@PathVariable(value = "id") String id,Model model,HttpServletRequest httpServletRequest){
        if(articleService.findArticleById(id)==null)
        {
            return "error/ArticleNotFound";
        }
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession!=null)
        {
            try {
                User user = (User) httpSession.getAttribute("user");
                Article article = articleService.findArticleById(id);
                if(user.getId().equals(article.getAuthorId()))
                {
                    model.addAttribute("ScreenName",new Gson().toJson(user.getScreenName()));
                }
                model.addAttribute("Type",new Gson().toJson(typeService.getById(article.getTypeId())));
                model.addAttribute("Article",new Gson().toJson(article));
            }catch (Exception e)
            {

            }
        }
        return "editBlog";
    }

    @ResponseBody
    @PostMapping(value="/create")
    public String createNewArticle(@RequestBody @Valid Article article, BindingResult result, HttpServletRequest httpServletRequest){
        if(result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
            return "error";
        }
        else {
            try {
                HttpSession httpSession = httpServletRequest.getSession(false);
                User user = (User)httpSession.getAttribute("user");
                System.out.println(user);
                article.setAuthor(user.getScreenName());
                article.setAuthorId(user.getId());
                articleService.addArticle(article);
                return new Gson().toJson(new Response(true,"Submit success"));
            } catch (Exception e) {
                return new Gson().toJson(new Response(false,e.getMessage()));
            }
        }
    }

    @ResponseBody
    @PostMapping(value="/update")
    public String updateArticle(@RequestBody Article article) {
        try {
            articleService.updateArticle(article);
            return new Gson().toJson(new Response(true,"提交成功"));
        } catch (Exception e) {
            return new Gson().toJson(new Response(false,e.getMessage()));
        }
    }

    @RequestMapping("/post")
    public String postNewBlog(Model model,HttpServletRequest httpServletRequest){
        model.addAttribute("Types",new Gson().toJson(typeService.getAllTypes()));
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession!=null)
        {
            try {
                User user = (User) httpSession.getAttribute("user");
                model.addAttribute("ScreenName", new Gson().toJson(user.getScreenName()));
            }catch (Exception e)
            {

            }
        }
        return "postNewBlog";
    }
}
