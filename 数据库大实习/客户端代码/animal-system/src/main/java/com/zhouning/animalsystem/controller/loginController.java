package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.User;
import com.zhouning.animalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class loginController {

    @RequestMapping("/login")
    public  String to_login(){

        return "login";
    }

    @RequestMapping("/")
    public  String login(Model model){
        model.addAttribute("message", true);
        return "login";
    }
}
