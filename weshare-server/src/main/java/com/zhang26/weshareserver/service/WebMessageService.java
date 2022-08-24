package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.WebMessageMapper;
import model.WebMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public
class WebMessageService {

    @Autowired
    WebMessageMapper webMessageMapper;

    public int insert(WebMessage webMessage){
        return webMessageMapper.insert(webMessage);
    }
    public int update(int id){
        return webMessageMapper.update(id);
    }
    public int updateAll(String userName){
        return webMessageMapper.updateAll(userName);
    }
    public List<WebMessage> getReadByUserName(String userName){
        return webMessageMapper.getReadByUserName(userName);
    }
    public List<WebMessage> getUnReadByUserName(String userName){
        return webMessageMapper.getUnReadByUserName(userName);
    }
    public int getUnReadNumByUserName(String userName){
        return webMessageMapper.getUnReadNumByUserName(userName);
    }
}