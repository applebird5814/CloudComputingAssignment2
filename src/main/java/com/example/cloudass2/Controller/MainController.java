package com.example.cloudass2.Controller;


import com.example.cloudass2.Entity.User;
import com.example.cloudass2.Service.ArticlesService;
import com.example.cloudass2.Service.TypesService;
import com.example.cloudass2.util.BigQueryUtil;
import com.example.cloudass2.util.COVIDnode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author miaos
 */
@Controller
public class MainController {

    @Autowired
    @Qualifier("typesServiceImpl")
    TypesService typesService;

    @Autowired
    @Qualifier("articlesServiceImpl")
    ArticlesService articlesService;

    @RequestMapping("/*")
    public String index(Model model, HttpServletRequest httpServletRequest) {
        model.addAttribute("Types",new Gson().toJson(typesService.getAllTypes()));
        model.addAttribute("LatestArticle",new Gson().toJson(articlesService.findAll().get(0)));
        HttpSession httpSession = httpServletRequest.getSession();
        try {
            User user = (User) httpSession.getAttribute("user");
            if(user!=null) {
                model.addAttribute("ScreenName", new Gson().toJson(user.getScreenName()));
            }
        }catch (Exception e)
        {
        }
        try{
            if(httpSession.getAttribute("COVID")==null){
                httpSession.setAttribute("COVID",new Gson().toJson(loadCOVID19data()));
            }
            model.addAttribute("COVID19",httpSession.getAttribute("COVID"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try{
            String address = (String) httpSession.getAttribute("address");
            if(address!=null) {
                model.addAttribute("Address", new Gson().toJson(address));
            }
        }catch (Exception e)
        {
        }
        return "index";
    }

    private List<String> loadCOVID19data() throws InterruptedException {
        BigQueryUtil bigQueryUtil = new BigQueryUtil();
        HashMap<String,COVIDnode> map = bigQueryUtil.getCOD19("Australia");
        ArrayList<String> list=new ArrayList<>();
        for (String key:map.keySet())
        {
            list.add(map.get(key).getOutPrintString());
        }
        return list;
    }

    @ResponseBody
    @RequestMapping("/addAddress")
    public String address(@RequestParam("address") String address,HttpServletRequest httpServletRequest){
        String[] a =address.split(" ");
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
