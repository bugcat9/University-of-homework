package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.animal;
import com.zhouning.animalsystem.service.animalService;
import com.zhouning.animalsystem.service.animalServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//动物显示的控制器
@Controller
public class animalController {

    @Autowired
    animalService service;

    @RequestMapping("/animal")
    public String animal(Model model){
        List<animal> animals = service.getanimals();
        model.addAttribute("animals",animals);
        return "animal";
    }

    @RequestMapping("/animal-grid")
    public String getanimal_grid(){
        return "animal-grid";
    }

    //提供给前端的接口
    @RequestMapping("/animals")
    @ResponseBody
    public List<animal> animals(){
        List<animal> animals = service.getanimals();
        return animals;
    }



    //提供给前端的接口
    @RequestMapping("/animal-insert")
    @ResponseBody
    public String insert(animal a){
        System.out.println(a.getAnimal_name());
        boolean isok= service.insertAnimal(a.getAnimal_ID(), a.getAnimal_name(),
                a.getAnimal_species(), a.getAnimal_age(), a.getShelter_ID());
        if (isok)
            return "插入成功";
        return "插入失败";
    }

    //提供给前端的接口
    @RequestMapping("/animal-update")
    @ResponseBody
    public String update(animal a){
        System.out.println(a.getAnimal_name());
        boolean isok= service.updateAnimal(a.getAnimal_ID(), a.getAnimal_name(),
                a.getAnimal_species(), a.getAnimal_age(), a.getShelter_ID());
        if (isok)
            return "更改成功";
        return "更改失败";
    }

    //提供给后端的接口
    @RequestMapping("/animal-delete")
    @ResponseBody
    public String delete(animal a){
        System.out.println(a.getAnimal_name());
        boolean isok= service.deleteAnimal(a.getAnimal_ID());
        if (isok)
            return "删除成功";
        return "删除失败";
    }

}
