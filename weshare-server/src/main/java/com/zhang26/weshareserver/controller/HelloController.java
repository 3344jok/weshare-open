package com.zhang26.weshareserver.controller;


import com.zhang26.weshareserver.config.ReturnStatus;
import com.zhang26.weshareserver.mapper.UserLoginMapper;
import model.TestPojo;
import model.UserLogin;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping
public class HelloController {
    @Autowired
    UserLoginMapper loginMapper;
    @GetMapping("/table.json")
    public HashMap<String,Object> table() {

        TestPojo user=new TestPojo(3,"张三",100000,"翻斗花园","睡觉",new Date(),"不到");
        List<TestPojo> list=new LinkedList<>();
        list.add(user);
        HashMap<String,Object> ret=new HashMap<>();
        ret.put("list",list);
        return ret;
    }

    @GetMapping("/hello")
    public ReturnJsonUtil<Object> hello() {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
//        UserLogin getByUserName(String name);
//
//        int getIdByUserName(String name);
//
//        int updateById(UserLogin userLogin);

        return rj;
    }


    @GetMapping("/user/common")
    public ReturnJsonUtil<Object> userCommonRequest(@AuthenticationPrincipal UserLogin user) {
        System.out.println("User: "+user.getUsername()+" Authorities: "+user.getAuthorities());
        return new ReturnJsonUtil<>(ReturnStatus.SUCCESS);
    }

    @GetMapping("/admin/common")
    public ReturnJsonUtil<Object> adminCommonRequest(@AuthenticationPrincipal UserLogin user) {
        System.out.println("User: "+user.getUsername()+" Authorities: "+user.getAuthorities());
        return new ReturnJsonUtil<>(ReturnStatus.SUCCESS);
    }

}