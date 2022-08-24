package com.zhang26.weshareserver.controller;


import com.zhang26.weshareserver.config.ReturnStatus;
import model.UserInfo;
import model.WebMessage;
import com.zhang26.weshareserver.service.UserInfoService;
import com.zhang26.weshareserver.service.WebMessageService;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/header")
public class headerController {
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    WebMessageService webMessageService;

    @GetMapping("/getuserinfo")
    public ReturnJsonUtil<Object> getUserInfo(String username) {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        UserInfo userInfo = userInfoService.getByUserName(username);
        try {
            if (userInfo != null) {
                rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
                rj.setData(userInfo);
            } else {
                rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
            }
        } catch (Exception e) {
            rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
        }
        return rj;
    }

    @GetMapping("/getmessages")
    public ReturnJsonUtil<Object> getMessages(String username) {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try {
            List<WebMessage> read=webMessageService.getReadByUserName(username);
            List<WebMessage> unread=webMessageService.getUnReadByUserName(username);
            int unreadLen=webMessageService.getUnReadNumByUserName(username);
            HashMap<String,Object> map=new HashMap<>();
            map.put("read",read);
            map.put("unread",unread);
            map.put("unreadLen",unreadLen);
            rj.setData(map);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        } catch (Exception e) {
            rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
        }
        return rj;
    }
}
