package com.zhouning.animalsystem.controller;

import com.zhouning.animalsystem.entity.vaccineRecord;
import com.zhouning.animalsystem.service.vaccineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class vaccineRecordController {

    @Autowired
    vaccineRecordService service;

    @RequestMapping("/vaccineRecord")
    public String vaccineRecord(Model model){
        List<vaccineRecord> vaccineRecords = service.getvaccineRecords();
        System.out.println("大小为："+vaccineRecords.size());
        model.addAttribute("vaccineRecords",vaccineRecords);
        return "vaccineRecord";
    }

    @RequestMapping("/vaccineRecord-grid")
    public String getvaccineRecord_grid(){
        return "vaccineRecord-grid";
    }

    //提供给前端的接口
    @RequestMapping("/vaccineRecords")
    @ResponseBody
    public List<vaccineRecord> vaccineRecords(){
        List<vaccineRecord> vaccineRecords = service.getvaccineRecords();
        System.out.println("大小为："+vaccineRecords.size());
        return vaccineRecords;
    }



    //提供给前端的接口
    @RequestMapping("/vaccineRecord-insert")
    @ResponseBody
    public String insert(vaccineRecord v){
        System.out.println(v.getRemarks());
        boolean isok= service.insertvaccineRecord(v.getVaccine_ID(),v.getAnimal_ID(),v.getUser_ID(),v.getVaccination_time(),v.getRemarks());
        if (isok)
            return "插入成功";
        return "插入失败";
    }

    //提供给前端的接口
    @RequestMapping("/vaccineRecord-update")
    @ResponseBody
    public String update(vaccineRecord v){
        System.out.println(v.getRemarks());
        boolean isok= service.updatevaccineRecord(v.getVaccine_ID(),v.getAnimal_ID(),v.getUser_ID(),v.getVaccination_time(),v.getRemarks());
        if (isok)
            return "更新成功";
        return "更新失败";
    }

    //提供给后端的接口
    @RequestMapping("/vaccineRecord-delete")
    @ResponseBody
    public String delete(vaccineRecord v){
        System.out.println(v.getRemarks());
        boolean isok= service.deletevaccineRecord(v.getVaccine_ID(),v.getAnimal_ID(),v.getUser_ID(),v.getVaccination_time());
        if (isok)
            return "删除成功";
        return "删除失败";
    }
}
