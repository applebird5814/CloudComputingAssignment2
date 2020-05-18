package com.example.cloudass2.Controller;

import com.example.cloudass2.Entity.Article;
import com.example.cloudass2.Entity.Response;
import com.example.cloudass2.Entity.User;
import com.example.cloudass2.Service.ArticlesService;
import com.example.cloudass2.Service.CommentsService;
import com.example.cloudass2.Service.TypesService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author miaos
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    @Qualifier("articlesServiceImpl")
    ArticlesService articlesService;

    @Autowired
    @Qualifier("typesServiceImpl")
    TypesService typesService;

    @Autowired
    @Qualifier("commentsServiceImpl")
    CommentsService commentsService;

    @RequestMapping("/blog")
    public String noBlogId(){
        return "error/ArticleNotFound";
    }

    @RequestMapping("/blog/{id}")
    public String blog(@PathVariable(value = "id") String id,Model model,HttpServletRequest httpServletRequest){
        Article article = articlesService.findArticleById(id);
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
        model.addAttribute("Comments",new Gson().toJson(commentsService.findCommentsByArticleId(id)));
        return "blog";
    }

    @RequestMapping("/blogList")
    public String blogList(Model model){
        model.addAttribute("Articles",new Gson().toJson(articlesService.findAll()));
        return "blogList";
    }

    @RequestMapping("/blogList/{id}")
    public String blogList(Model model, @PathVariable String id){
        model.addAttribute("Articles",new Gson().toJson(articlesService.findByType(id)));
        return "blogList";
    }

    @RequestMapping("/editBlog/{id}")
    public String editBlog(@PathVariable(value = "id") String id,Model model,HttpServletRequest httpServletRequest){
        if(articlesService.findArticleById(id)==null)
        {
            return "error/ArticleNotFound";
        }
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession!=null)
        {
            try {
                User user = (User) httpSession.getAttribute("user");
                Article article = articlesService.findArticleById(id);
                if(user.getId().equals(article.getAuthorId()))
                {
                    model.addAttribute("AuthorValidation",new Gson().toJson(user.getScreenName()));
                }
                model.addAttribute("Type",new Gson().toJson(typesService.getById(article.getTypeId())));
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
                articlesService.addArticle(article);
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
            articlesService.updateArticle(article);
            return new Gson().toJson(new Response(true,"提交成功"));
        } catch (Exception e) {
            return new Gson().toJson(new Response(false,e.getMessage()));
        }
    }

    @RequestMapping("/post")
    public String postNewBlog(Model model,HttpServletRequest httpServletRequest){
        model.addAttribute("Types",new Gson().toJson(typesService.getAllTypes()));
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
