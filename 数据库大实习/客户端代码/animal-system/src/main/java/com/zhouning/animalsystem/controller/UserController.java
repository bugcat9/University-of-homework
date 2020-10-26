package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.User;
import com.zhouning.animalsystem.repository.UserRepository;
import com.zhouning.animalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/checklogin")
    public String isCorrect(String userid, String password, Model model, HttpSession session){
        System.out.println(userid+"    "+password);
        model.addAttribute("message", false);
        boolean  iscorrect = userService.isCorrect(userid, password);
        if(iscorrect){
            User user = userService.getUser(userid);
            model.addAttribute("user",user);
            session.setAttribute("user", user);
            return "index";
        }
        return "login";
    }

}
