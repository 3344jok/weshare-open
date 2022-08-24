package com.zhang26.weshareserver.controller;


//import com.zhang26.weshareserver.component.WebSocketServer;
import com.zhang26.weshareserver.config.ReturnStatus;
import model.WebMessage;
import com.zhang26.weshareserver.service.WebMessageService;
//import com.zhang26.weshareserver.service.WebSocketService;
import com.zhang26.weshareserver.service.WebSocketService;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path = "/tabs")
public class TabsController {

    @Autowired
    WebMessageService webMessageService;

    @Autowired
    WebSocketService webSocketService;
    @GetMapping("/readall")
    public ReturnJsonUtil<Object> readAll(String userName) {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try {
            webMessageService.updateAll(userName);
            webSocketService.sendMsg(userName);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        } catch (Exception e) {
            rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
        }
        return rj;
    }

    @GetMapping("/readone")
    public ReturnJsonUtil<Object> readAll(int messageId,String userName) {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try {
            webMessageService.update(messageId);
            webSocketService.sendMsg(userName);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        } catch (Exception e) {
            rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
        }
        return rj;
    }
}
