package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.animal;
import com.zhouning.animalsystem.entity.health;
import com.zhouning.animalsystem.service.healthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class healthController {

    @Autowired
    healthService service;

    @RequestMapping("/health")
    public String health(Model model){
        List<health> all_health = service.get_all_health();
        model.addAttribute("all_health",all_health);
        return "health";
    }

    @RequestMapping("/health-grid")
    public String get_health_grid(){
        return "health-grid";
    }

    //提供给前端的接口
    @RequestMapping("/all_health")
    @ResponseBody
    public List<health> animals(){
        List<health> all_health = service.get_all_health();
        return all_health;
    }



    //提供给前端的接口
    @RequestMapping("/health-insert")
    @ResponseBody
    public String insert(health h){
        System.out.println(h.getHealth_information());
        boolean isok= service.insertHealth(h.getAnimal_ID(),h.getUser_ID(),h.getCheck_date().toString(),
                h.getHealth_information(), h.getRemarks());
        if (isok)
            return "插入成功";
        return "插入失败";
    }

    //提供给前端的接口
    @RequestMapping("/health-update")
    @ResponseBody
    public String update(health h){
        System.out.println(h.getHealth_information());
        boolean isok= service.updateHealth(h.getAnimal_ID(),h.getUser_ID(),h.getCheck_date(),
                h.getHealth_information(), h.getRemarks());
        if (isok)
            return "更改成功";
        return "更改失败";
    }

    //提供给后端的接口
    @RequestMapping("/health-delete")
    @ResponseBody
    public String delete(health h){
        System.out.println(h.getHealth_information());
        boolean isok= service.deleteHealth(h.getAnimal_ID(),h.getUser_ID(),h.getCheck_date().toString());
        if (isok)
            return "删除成功";
        return "删除失败";
    }
}
