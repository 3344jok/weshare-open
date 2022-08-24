package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.MailSendLogMapper;
import model.MailSendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MailSendLogService {
    @Autowired
    MailSendLogMapper mailSendLogMapper;
    public Integer updateMailSendLogStatus(String msgId, Integer status) {
        return mailSendLogMapper.updateMailSendLogStatus(msgId, status);
    }

    public Integer insert(MailSendLog mailSendLog) {
        return mailSendLogMapper.insert(mailSendLog);
    }

    public List<MailSendLog> getMailSendLogsByStatus() {
        return mailSendLogMapper.getMailSendLogsByStatus();
    }

    public Integer updateCount(String msgId, Date date) {
        return mailSendLogMapper.updateCount(msgId,date);
    }

    public MailSendLog createMailSendLog(String msgId,int status,String routeKey,String exchange,Date tryTime,Date createTime,int type,int parentId,int childId){
        MailSendLog mailSendLog=new MailSendLog();
        mailSendLog.setMsgId(msgId);
        mailSendLog.setStatus(status);
        mailSendLog.setRouteKey(routeKey);
        mailSendLog.setExchange(exchange);
        mailSendLog.setTryTime(tryTime);
        mailSendLog.setCreateTime(createTime);
        mailSendLog.setType(type);
        mailSendLog.setParentId(parentId);
        mailSendLog.setChildId(childId);
        mailSendLogMapper.insert(mailSendLog);
        return mailSendLog;
    }
}
