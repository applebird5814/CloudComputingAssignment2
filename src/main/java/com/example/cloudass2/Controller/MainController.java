package com.example.cloudass2.Controller;


import com.example.cloudass2.Entity.User;
import com.example.cloudass2.service.ArticleService;
import com.example.cloudass2.service.TypeService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeService typeService;

    @Autowired
    @Qualifier("articleServiceImpl")
    ArticleService articleService;

    @RequestMapping("/test")
    public String test(@RequestParam(required = false,defaultValue = "1") int page, Model model){
        if(page<1)
        {
            page=1;
        }
        //让page可以从1开始，更符合人的阅读习惯
        page--;
        //每页显示的文章数量
        int size = 4;
        model.addAttribute("Articles",new Gson().toJson(articleService.findPage(page,size).getContent()));
        System.out.println(new Gson().toJson(articleService.findPage(page,size)));
        return "test";
    }

    @RequestMapping("/*")
    public String index(Model model, HttpServletRequest httpServletRequest){
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
            try{
                String address = (String) httpSession.getAttribute("address");
                System.out.println(address);
                model.addAttribute("Address",new Gson().toJson(address));
            }catch (Exception e)
            {

            }
        }
        return "index";
    }

    @ResponseBody
    @RequestMapping("/addAddress")
    public String address(@RequestParam("address") String address,HttpServletRequest httpServletRequest){
        System.out.println(address);
        String a[]=address.split(" ");
        HttpSession httpSession =httpServletRequest.getSession();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=2;i<a.length;i++)
        {
            stringBuilder.append(a[i]+" ");
        }
        httpSession.setAttribute("address",stringBuilder.toString());
        // 30 minutes * 60 seconds
        httpSession.setMaxInactiveInterval(30*60);
        return "success";
    }


    @RequestMapping("/signIn")
    public String signIn(){
        return "signIn";
    }

    @RequestMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

}
