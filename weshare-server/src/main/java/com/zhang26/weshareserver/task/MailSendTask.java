package com.zhang26.weshareserver.task;

import model.*;
import com.zhang26.weshareserver.service.GroupService;
import com.zhang26.weshareserver.service.MailSendLogService;
import com.zhang26.weshareserver.service.UserInfoService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class MailSendTask {
    @Autowired
    MailSendLogService mailSendLogService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    GroupService groupService;
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailResendTask() {
        List<MailSendLog> logs = mailSendLogService.getMailSendLogsByStatus();
        if (logs == null || logs.size() == 0) {
            return;
        }

        logs.forEach(mailSendLog->{
            if (mailSendLog.getCount() >= 3) {
                mailSendLogService.updateMailSendLogStatus(mailSendLog.getMsgId(), 2);//直接设置该条消息发送失败
            }else{
                mailSendLogService.updateCount(mailSendLog.getMsgId(), new Date());
                if(mailSendLog.getType()==MailConstants.REGISTER){
                    UserInfo userInfo=userInfoService.getByUserId(mailSendLog.getParentId());
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("mailSendLog",mailSendLog);
                    map.put("user",userInfo);
                    rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_REGISTER, map, new CorrelationData(mailSendLog.getMsgId()));
                }
                else if(mailSendLog.getType()==MailConstants.GROUP_JOIN){
                    Group group=groupService.getByGroupId(mailSendLog.getParentId());
                    UserInfo userInfo=userInfoService.getByUserId(mailSendLog.getChildId());
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("mailSendLog",mailSendLog);
                    map.put("group",group);
                    map.put("user",userInfo);
                    rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_GROUP,map,new CorrelationData(mailSendLog.getMsgId()));
                }
//                else if(mailSendLog.getType()==MailConstants.GROUP_ADD||
//                mailSendLog.getType()==MailConstants.GROUP_DEL||mailSendLog.getType()==MailConstants.GROUP_UPD
//                ||mailSendLog.getType()==MailConstants.GROUP_JOIN){
//                    rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_GROUP, mailSendLog, new CorrelationData(mailSendLog.getMsgId()));
//                }
//                else{
//                    rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_ALBUM, mailSendLog, new CorrelationData(mailSendLog.getMsgId()));
//                }
            }
        });
    }
}
