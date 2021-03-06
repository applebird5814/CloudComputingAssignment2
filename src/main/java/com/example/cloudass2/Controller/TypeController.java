package com.example.cloudass2.Controller;

import com.example.cloudass2.Entity.Response;
import com.example.cloudass2.Entity.Type;
import com.example.cloudass2.Service.TypesService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

/**
 * @author miaos
 */
@Controller
@RequestMapping("/type")
public class TypeController {

    @Autowired
    @Qualifier("typesServiceImpl")
    TypesService typesService;

    @ResponseBody
    @PostMapping("/addType")
    public String addType(@RequestBody Type type, BindingResult result){
        if(result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
            return new Gson().toJson(new Response(false,"请不要输入非法字符！"));
        }
        else {
            try {
                typesService.addType(type);
                return new Gson().toJson(new Response(true,"提交成功"));
            } catch (Exception e) {
                return new Gson().toJson(new Response(false,"该类型已经存在！"));
            }
        }
    }

    @RequestMapping("/editType")
    public String typeEdit(Model model){
        model.addAttribute("Types",new Gson().toJson(typesService.getAllTypes()));
        return "editType";
    }

    @RequestMapping("/view")
    public String typeView(Model model){
        model.addAttribute("Types",new Gson().toJson(typesService.getAllTypes()));
        return "type";
    }

}
