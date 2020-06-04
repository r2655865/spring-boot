package com.activiti.controller;


import com.activiti.pojo.Hospitals;
import com.activiti.pojo.Page;
import com.activiti.pojo.Result;
import com.activiti.service.HospitalsService;
import com.activiti.utils.Md5;
import com.activiti.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/hospital")
public class HospitalsController {


    @Autowired
    private HospitalsService hospitalsService;


    @RequestMapping("/list")
    public String list(){
        return "hospitals/list";
    }

    @RequestMapping("/listQueryByPage")
    @ResponseBody
    public Object listQueryByPage(Page page){
        Map<String, Object> map = PageUtils.getFilterPage(page);
        return hospitalsService.listQueryByPage(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public Object add(Hospitals hospitals){
        Result result = new Result();

        if(StringUtils.isBlank(hospitals.getId())){
            hospitals.setSignCode(Md5.getSignCode());
            result = hospitalsService.add(hospitals);
        }else{
            result = hospitalsService.updateByPrimaryKeySelective(hospitals);
        }
        return result;
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") String id){
        return hospitalsService.deleteById(id);
    }
}
