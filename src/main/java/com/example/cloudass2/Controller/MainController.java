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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
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
    public String test(){
        return "test";
    }

    @ResponseBody
    @RequestMapping("/addImage")
    private String addImage(@RequestParam("file")MultipartFile file){
        if(!file.isEmpty())
        {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //后缀名可用于判断是否是图片
            String filePath = "src/main/resource/static/images";
            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);

                return "上传成功";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "error";
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
