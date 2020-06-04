package com.activiti.controller;


import com.activiti.pojo.Accounts;
import com.activiti.pojo.Result;
import com.activiti.service.AccountsService;
import com.activiti.utils.Md5;
import com.activiti.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private AccountsService accountsService;

    @RequestMapping("/login")
    public String goLogin() {
        return "/login";
    }

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/login";
    }


    @PostMapping("/loginSign")
    @ResponseBody
    public Object login(@RequestParam("userName") String userName,@RequestParam("passWord") String passWord, HttpSession session){

        Result result = new Result();
        if(StringUtils.isBlank(userName)){
            result =  ResultUtil.operatDataResult(0,null,"用户名不能为空!");
        }else if(StringUtils.isBlank(passWord)){
            result =  ResultUtil.operatDataResult(0,null,"密码不能为空!");
        }else{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("userName",userName);
            map.put("passWord",Md5.md5password(passWord));
            Accounts accounts = accountsService.selectByParams(map);
            if(accounts == null){
                result = ResultUtil.operatDataResult(0,null,"请查证用户在进行登录!");
            }else{
                session.setAttribute("user", accounts);
                result = ResultUtil.operatDataResult(1,null,"/home");
            }
        }
        return result;
    }


    @PostMapping("/updatePassWord")
    @ResponseBody
    public Object updatePassWord(@RequestParam("passWord") String passWord,@RequestParam("isPwd") String isPwd, HttpSession session){
        Result result = new Result();

        //校验两次输入的密码是否相同
        Accounts accounts = (Accounts) session.getAttribute("user");
        if(passWord.equals(isPwd)){
            //修改密码
            accounts.setPassWord(Md5.md5password(passWord));
            result = accountsService.updateByPrimaryKeySelective(accounts);
        }else{
            result = ResultUtil.operatDataResult(0,null,"两次密码不一致!");
        }

        return result;
    }
}
