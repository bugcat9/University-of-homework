package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.shelter;
import com.zhouning.animalsystem.service.shelterService;
import com.zhouning.animalsystem.service.shelterServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class shelterController {

    @Autowired
    shelterService service;

    @RequestMapping("/shelter")
    public String  shelter(Model model){
        List<shelter> shelters = service.getshelters();
        model.addAttribute("shelters", shelters);
        return "shelter";
    }

    @RequestMapping("/shelter-grid")
    public String getshelter_grid(){
        return "shelter-grid";
    }

    //提供给前端的接口
    @RequestMapping("/shelters")
    @ResponseBody
    public List<shelter>  getshelters(){
        List<shelter> shelters = service.getshelters();
        return shelters;
    }


    @RequestMapping("/shelter-insert")
    @ResponseBody
    public String insert(shelter s){
        System.out.println(s.getShelter_name());
        boolean isok= service.insertShelter(s.getShelter_id(),s.getShelter_name(),s.getShelter_address(),
        s.getPostcode(),s.getSum_rooms(),s.getRemain_rooms(),s.getShelter_comment());

        if (isok)
            return "插入成功";
        return "插入失败";
    }

    @RequestMapping("/shelter-update")
    @ResponseBody
    public String update(shelter s){
        System.out.println(s.getShelter_name());
        boolean isok= service.updateShelter(s.getShelter_id(),s.getShelter_name(),s.getShelter_address(),
                s.getPostcode(),s.getSum_rooms(),s.getRemain_rooms(),s.getShelter_comment());

        if (isok)
            return "更新成功";
        return "更新失败";
    }

    @RequestMapping("/shelter-delete")
    @ResponseBody
    public String delete(shelter s){
        System.out.println(s.getShelter_name());
        boolean isok= service.deleteShelter(s.getShelter_id());

        if (isok)
            return "删除成功";
        return "删除失败";
    }



}
