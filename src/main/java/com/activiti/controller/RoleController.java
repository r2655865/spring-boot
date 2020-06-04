package com.activiti.controller;


import com.activiti.pojo.Page;
import com.activiti.pojo.Result;
import com.activiti.pojo.Role;
import com.activiti.service.RoleService;
import com.activiti.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/list")
    public String list(){
        return "role/list";
    }


    @RequestMapping("/listQueryByPage")
    @ResponseBody
    public Object listQueryByPage(Page page){
        Map<String, Object> map = PageUtils.getFilterPage(page);
        return roleService.listQueryByPage(map);
    }

    @RequestMapping("/listSelectQuery")
    @ResponseBody
    public Object listSelectQuery(@RequestParam Map<String, Object> map){
        return roleService.listSelectQuery(map);
    }

    @RequestMapping("/add")
    @ResponseBody
    public Object add(Role role){

        Result result = new Result();
       if(StringUtils.isBlank(role.getId())){
           result = roleService.add(role);
       }else{
           result = roleService.updateByPrimaryKeySelective(role);
       }

        return result;
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") String id){
        return roleService.deleteById(id);
    }
}
