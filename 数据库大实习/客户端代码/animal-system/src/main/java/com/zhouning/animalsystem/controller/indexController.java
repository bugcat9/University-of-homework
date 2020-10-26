package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class indexController {

    @RequestMapping("/index")
    public String index(HttpSession session, Model model)
    {
        User user = (User) session.getAttribute("user");

        if (user!=null){
            model.addAttribute("user", user);
            return "index";
        }


        return "login";
    }

    @RequestMapping("/calender")
    public String calender(){
        return "calender";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }



}
