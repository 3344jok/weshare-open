package com.zhang26.messageserver.receiver;

import com.rabbitmq.client.Channel;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class MailReceiver {

    public static final Logger logger = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MailProperties mailProperties;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    StringRedisTemplate redisTemplate;



    private void sendMail(String content, String subject, UserInfo userInfo, MimeMessage msg, MimeMessageHelper helper, Long tag, Channel channel, String msgId) throws IOException {
        try {
            helper.setTo(userInfo.getEmailAcount());
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject(subject);
            helper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("name", userInfo.getUserName());
            context.setVariable("content", content);
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            javaMailSender.send(msg);
            redisTemplate.opsForHash().put("mail_log", msgId, "weshare");
            channel.basicAck(tag, false);
            logger.info(msgId + ":邮件发送成功");

        } catch (MessagingException e) {
            channel.basicNack(tag, false, true);
            e.printStackTrace();
            logger.error("邮件发送失败：" + e.getMessage());
        } catch (MailException e) {
            channel.basicNack(tag, false, true);
            e.printStackTrace();
            logger.error("邮件发送失败：" + e.getMessage());
        }
    }

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_REGISTER)
    public void redisterHandler(Message message, Channel channel) throws IOException {
        HashMap<String, Object> msgMap = (HashMap<String, Object>) message.getPayload();
        UserInfo userInfo = (UserInfo) msgMap.get("user");
        MailSendLog mailSendLog = (MailSendLog) msgMap.get("mailSendLog");
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            //redis 中包含该 key，说明该消息已经被消费过
            logger.info(msgId + ":消息已经被消费");
            channel.basicAck(tag, false);//确认消息已消费
            return;
        }
        //收到消息，发送邮件
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setTo(userInfo.getEmailAcount());
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("注册成功");
            helper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("name", userInfo.getUserName());
            String mail = templateEngine.process("registerMail", context);
            helper.setText(mail, true);
            javaMailSender.send(msg);
            redisTemplate.opsForHash().put("mail_log", msgId, "weshare");
            channel.basicAck(tag, false);
            logger.info(msgId + ":邮件发送成功");
        } catch (MessagingException e) {
            channel.basicNack(tag, false, true);
            e.printStackTrace();
            logger.error("邮件发送失败：" + e.getMessage());
        }
    }

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_GROUP)
    public void groupHandler(Message message, Channel channel) throws IOException {
        HashMap<String, Object> msgMap = (HashMap<String, Object>) message.getPayload();
        MailSendLog mailSendLog = (MailSendLog) msgMap.get("mailSendLog");
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            //redis 中包含该 key，说明该消息已经被消费过
            logger.info(msgId + ":消息已经被消费");
            channel.basicAck(tag, false);//确认消息已消费
            return;
        }
        //收到消息，发送邮件
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            List<UserInfo> userInfoList = (List<UserInfo>) msgMap.get("users");
            logger.info("group查看接收者");
            logger.info(userInfoList.toString());
            if (mailSendLog.getType() == MailConstants.GROUP_ADD) {
                Group group = (Group) msgMap.get("group");
                Album album = (Album) msgMap.get("album");
                String content = "您参与的相册集 " + group.getGroupName() + " 新增相册 " + album.getAlbumName() + " 。";
                for (UserInfo userInfo : userInfoList) {
                    sendMail(content, "新增相册", userInfo, msg, helper,tag,channel,msgId);
                }
            } else if (mailSendLog.getType() == MailConstants.GROUP_DEL) {
                Group group = (Group) msgMap.get("group");
                String content = "您参与的相册集 " + group.getGroupName() + " 已被删除 。";
                for (UserInfo userInfo : userInfoList) {
                    sendMail(content, "相册集删除", userInfo, msg, helper,tag,channel,msgId);
                }
            } else if (mailSendLog.getType() == MailConstants.GROUP_UPD) {
                Group oldGroup = (Group) msgMap.get("oldGroup");
                Group newGroup = (Group) msgMap.get("newGroup");
                String content = "您参与的相册集 " + oldGroup.getGroupName() + " 有了新的更改:  "
                        + oldGroup.getGroupName() + " -> " + newGroup.getGroupName() + "。";
                for (UserInfo userInfo : userInfoList) {
                    sendMail(content, "相册集更改", userInfo, msg, helper,tag,channel,msgId);

                }
            } else if (mailSendLog.getType() == MailConstants.GROUP_JOIN) {
                Group group = (Group) msgMap.get("group");
                UserInfo user = (UserInfo) msgMap.get("user");
                String content = "您参与的相册集 " + group.getGroupName() + " 新增成员： " + user.getUserName() + " 。";
                for (UserInfo userInfo : userInfoList) {
                    sendMail(content, "相册集新增成员", userInfo, msg, helper,tag,channel,msgId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("邮件发送失败：" + e.getMessage());
        }
    }

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_ALBUM)
    public void albumHandler(Message message, Channel channel) throws IOException {
        HashMap<String, Object> msgMap = (HashMap<String, Object>) message.getPayload();
        MailSendLog mailSendLog = (MailSendLog) msgMap.get("mailSendLog");
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            //redis 中包含该 key，说明该消息已经被消费过
            logger.info(msgId + ":消息已经被消费");
            channel.basicAck(tag, false);//确认消息已消费
            return;
        }
        //收到消息，发送邮件
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            List<UserInfo> userInfoList = (List<UserInfo>) msgMap.get("users");
            logger.info("album查看接收者");
            logger.info(userInfoList.toString());
            if (mailSendLog.getType() == MailConstants.ALBUM_DEL) {
                Album album = (Album) msgMap.get("album");
                UserInfo user = (UserInfo) msgMap.get("user");
                String content = "您参与的相册 " + album.getAlbumName() + " 已被删除。";
                for (UserInfo userInfo : userInfoList) {
                    sendMail(content, "相册删除", userInfo, msg, helper,tag,channel,msgId);
                }
            } else if (mailSendLog.getType() == MailConstants.ALBUM_UPD) {
                Album oldAlbum = (Album) msgMap.get("oldAlbum");
                Album newAlbum = (Album) msgMap.get("newAlbum");
                UserInfo user = (UserInfo) msgMap.get("user");
                String content = "您参与的相册 " + oldAlbum.getAlbumName() + " 有了新的更改:  "
                        + oldAlbum.getAlbumName() + " -> " + newAlbum.getAlbumName() + "。";
                for (UserInfo userInfo : userInfoList) {
                    sendMail(content, "相册更新", userInfo, msg, helper,tag,channel,msgId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("邮件发送失败：" + e.getMessage());
        }
    }
}