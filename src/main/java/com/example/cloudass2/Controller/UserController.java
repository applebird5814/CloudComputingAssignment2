package com.example.cloudass2.Controller;

import com.example.cloudass2.Entity.Response;
import com.example.cloudass2.Entity.User;

import com.example.cloudass2.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.*;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @ResponseBody
    @PostMapping(value="/signUp")
    public String userRegister(@RequestBody @Valid User user, BindingResult result)
    {
        if(result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
            return new Gson().toJson(new Response(false,"Invalid input!"));
        }
        boolean b = userService.createUser(user);
        if(b)
        {
            return new Gson().toJson(new Response(true,"Register success! Please login!"));
        }
        return new Gson().toJson(new Response(false,"Username or E-mail has already be used"));
    }

    @ResponseBody
    @PostMapping(value="/signIn")
    public String userSignIn(@RequestBody User user, HttpServletRequest httpServletRequest){

        Optional<User> optionalUser = userService.login(user.getUsername(), user.getPassword());
        if (optionalUser.isPresent()) {
            HttpSession httpSession =httpServletRequest.getSession();
            httpSession.setAttribute("user",optionalUser.get());
            // 30 minutes * 60 seconds
            httpSession.setMaxInactiveInterval(30*60);
            String sessionId = httpSession.getId();

            String welcome = "Welcome Back, "+optionalUser.get().getScreenName();
            return new Gson().toJson(new Response(true,welcome));
        } else {
            String error = "Your username or password is incorrect!";
            return new Gson().toJson(new Response(true,error));
        }
    }

    @ResponseBody
    @RequestMapping("/signOut")
    public String signOut(HttpServletRequest httpServletRequest){
        HttpSession httpSession =httpServletRequest.getSession();
        httpSession.removeAttribute("user");
        return new Gson().toJson(new Response(true,"Sign Out Success!"));
    }


}
