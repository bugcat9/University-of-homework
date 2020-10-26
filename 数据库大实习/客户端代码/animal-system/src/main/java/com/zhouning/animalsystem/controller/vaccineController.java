package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.vaccine;
import com.zhouning.animalsystem.service.vaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class vaccineController {

    @Autowired
    vaccineService service;

    @RequestMapping("/vaccine")
    public String vaccine(Model model){
        List<vaccine> vaccines = service.getVaccines();
        model.addAttribute("vaccines",vaccines);
        return "vaccine";
    }

    @RequestMapping("/vaccine-grid")
    public String getvaccine_grid(){
        return "vaccine-grid";
    }

    //提供给前端的接口
    @RequestMapping("/vaccines")
    @ResponseBody
    public List<vaccine> vaccines(){
        List<vaccine> vaccines = service.getVaccines();
        return vaccines;
    }



    //提供给前端的接口
    @RequestMapping("/vaccine-insert")
    @ResponseBody
    public String insert(vaccine v){
        System.out.println(v.getVaccine_ID());
        boolean isok= service.insertVaccine(v.getVaccine_ID(),v.getVaccine_type(),v.getVaccine_name());
        if (isok)
            return "插入成功";
        return "插入失败";
    }

    //提供给前端的接口
    @RequestMapping("/vaccine-update")
    @ResponseBody
    public String update(vaccine v){
        System.out.println(v.getVaccine_ID());
        boolean isok= service.updateVaccine(v.getVaccine_ID(),v.getVaccine_type(),v.getVaccine_name());
        if (isok)
            return "更改成功";
        return "更改失败";
    }

    //提供给后端的接口
    @RequestMapping("/vaccine-delete")
    @ResponseBody
    public String delete(vaccine v){
        System.out.println(v.getVaccine_ID());
        boolean isok= service.deleteVaccine(v.getVaccine_ID());
        if (isok)
            return "删除成功";
        return "删除失败";
    }
}
