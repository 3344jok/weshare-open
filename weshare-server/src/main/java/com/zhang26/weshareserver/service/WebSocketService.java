package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.component.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static com.zhang26.weshareserver.component.WebSocketServer.webSocketMap;

@Slf4j
@Service
public class WebSocketService {
    @Autowired
    WebSocketServer webSocketServer;

    private static final String data="更新";
    public void sendMsg(String userName){
        ConcurrentHashMap<String,WebSocketServer> webSocketMap= WebSocketServer.webSocketMap;
        System.out.println(System.identityHashCode(webSocketMap));
        if(webSocketMap.containsKey(userName)){
            log.info("找到");
            WebSocketServer webSocketServer=webSocketMap.get(userName);
            try {
                log.info("websocket发送");
                webSocketServer.sendMessage(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
