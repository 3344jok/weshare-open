package com.zhang26.weshareserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import com.zhang26.weshareserver.util.DateUtil;
import com.zhang26.weshareserver.util.FormatUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class MessageService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MailSendLogService mailSendLogService;

    @Autowired
    UserGroupService userGroupService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupAlbumService groupAlbumService;

    @Autowired
    WebSocketService webSocketService;

    @Autowired
    WebMessageService webMessageService;



    private static final String registerFormat="%s ，欢迎使用weshare！";
    private static final String groupDelFormat="【相册集删除】 您参与的相册集 %s 已被删除！";
    private static final String groupUpdFormat="【相册集更新】 您参与的相册集 %s 有了新的信息更新：%s -> %s！";
    private static final String groupJoinFormat="【新增成员】 您参与的相册集 %s 有了新的成员：%s！";
    private static final String groupAddFormat="【新增相册】 您参与的相册集 %s 有了新的相册：%s！";
    private static final String albumDelFormat="【相册删除】 您参与的相册 %s 已被删除！";
    private static final String albumUpdFormat="【相册更新】 您参与的相册 %s 有了新的信息更新！";

    public void sendWebMsg(String content,String userName){
        WebMessage webMessage=new WebMessage();
        webMessage.setUserName(userName);
        webMessage.setContent(content);
        webMessage.setStatus(0);
        webMessage.setCreateDate(DateUtil.utilDateToSQLDate(new Date()));
        webMessageService.insert(webMessage);
        webSocketService.sendMsg(userName);
    }

    public void sendRegisterMsg(UserInfo userInfo) {
        MailSendLog mailSendLog = mailSendLogService.createMailSendLog(FormatUtil.getUUID32(), 0, MailConstants.MAIL_ROUTING_KEY_REGISTER, MailConstants.MAIL_EXCHANGE_NAME, new Date(), new Date(), MailConstants.REGISTER, userInfo.getId(), -1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mailSendLog", mailSendLog);
        map.put("user", userInfo);
        String content=String.format(registerFormat,userInfo.getUserName());
        sendWebMsg(content,userInfo.getUserName());
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_REGISTER, map, new CorrelationData(mailSendLog.getMsgId()));
    }

    public void sendGroupMsgAdd(Group group, Album album) {
        MailSendLog mailSendLog = mailSendLogService.createMailSendLog(FormatUtil.getUUID32(), 0, MailConstants.MAIL_ROUTING_KEY_GROUP, MailConstants.MAIL_EXCHANGE_NAME, new Date(), new Date(), MailConstants.GROUP_ADD, group.getId(), album.getId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("mailSendLog", mailSendLog);
        map.put("group", group);
        map.put("album", album);
        List<UserInfo> userInfos = getGroupMsgReceiver(group);
        map.put("users", userInfos);
        String content=String.format(groupAddFormat,group.getGroupName(),album.getAlbumName());
        for(UserInfo each:userInfos){
            sendWebMsg(content,each.getUserName());
        }
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_GROUP, map, new CorrelationData(mailSendLog.getMsgId()));
    }

    private List<UserInfo> getGroupMsgReceiver(Group group) {
        List<UserGroup> list = userGroupService.getByGroupId(group.getId());
        HashSet<String> set = new HashSet<>();
        List<UserInfo> userInfos = new LinkedList<>();
        for (UserGroup each : list) {
            UserInfo userInfo = userInfoService.getByUserId(each.getUserId());
            if (set.contains(userInfo.getUserName())) continue;
            else {
                set.add(userInfo.getUserName());
                userInfos.add(userInfo);
            }
        }
        return userInfos;
    }

    public void sendGroupMsgDel(Group group) {
        MailSendLog mailSendLog = mailSendLogService.createMailSendLog(FormatUtil.getUUID32(), 0, MailConstants.MAIL_ROUTING_KEY_GROUP, MailConstants.MAIL_EXCHANGE_NAME, new Date(), new Date(), MailConstants.GROUP_DEL, group.getId(), -1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mailSendLog", mailSendLog);
        map.put("group", group);
        List<UserInfo> userInfos = getGroupMsgReceiver(group);
        map.put("users", userInfos);
        String content=String.format(groupDelFormat,group.getGroupName());
        for(UserInfo each:userInfos){
            sendWebMsg(content,each.getUserName());
        }
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_GROUP, map, new CorrelationData(mailSendLog.getMsgId()));
    }

    public void sendGroupMsgUpd(Group oldGroup, Group newGroup) {
        MailSendLog mailSendLog = mailSendLogService.createMailSendLog(FormatUtil.getUUID32(), 0, MailConstants.MAIL_ROUTING_KEY_GROUP, MailConstants.MAIL_EXCHANGE_NAME, new Date(), new Date(), MailConstants.GROUP_UPD, newGroup.getId(), -1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mailSendLog", mailSendLog);
        map.put("oldGroup", oldGroup);
        map.put("newGroup", newGroup);
        List<UserInfo> userInfos = getGroupMsgReceiver(newGroup);
        map.put("users", userInfos);
        String content=String.format(groupUpdFormat,oldGroup.getGroupName(),oldGroup.getGroupName(),newGroup.getGroupName());
        for(UserInfo each:userInfos){
            sendWebMsg(content,each.getUserName());
        }
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_GROUP, map, new CorrelationData(mailSendLog.getMsgId()));
    }

    public void sendGroupMsgJoin(Group group, UserInfo userInfo) {
        MailSendLog mailSendLog = mailSendLogService.createMailSendLog(FormatUtil.getUUID32(), 0, MailConstants.MAIL_ROUTING_KEY_GROUP, MailConstants.MAIL_EXCHANGE_NAME, new Date(), new Date(), MailConstants.GROUP_JOIN, group.getId(), userInfo.getId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("mailSendLog", mailSendLog);
        map.put("group", group);
        map.put("user", userInfo);
        List<UserInfo> userInfos = getGroupMsgReceiver(group);
        map.put("users", userInfos);
        String content=String.format(groupJoinFormat,group.getGroupName(),userInfo.getUserName());
        for(UserInfo each:userInfos){
            sendWebMsg(content,each.getUserName());
        }
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_GROUP, map, new CorrelationData(mailSendLog.getMsgId()));
    }

    public void sendAlbumMsgDel(Album album) {
        MailSendLog mailSendLog = mailSendLogService.createMailSendLog(FormatUtil.getUUID32(), 0, MailConstants.MAIL_ROUTING_KEY_ALBUM, MailConstants.MAIL_EXCHANGE_NAME, new Date(), new Date(), MailConstants.ALBUM_DEL, album.getId(), -1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mailSendLog", mailSendLog);
        map.put("album", album);
        Group group = getGroup(album);
        if (group == null) return;
        List<UserInfo> userInfos = getGroupMsgReceiver(group);
        map.put("users", userInfos);
        String content=String.format(albumDelFormat,album.getAlbumName());
        for(UserInfo each:userInfos){
            sendWebMsg(content,each.getUserName());
        }
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_ALBUM, map, new CorrelationData(mailSendLog.getMsgId()));
    }

    public Group getGroup(Album album) {
        List<GroupAlbum> groupAlbums = groupAlbumService.getByAlbumId(album.getId());
        if (groupAlbums == null) return null;
        GroupAlbum groupAlbum = groupAlbums.get(0);
        int groupId = groupAlbum.getGroupId();
        Object group = groupService.getByGroupId(groupId);
        return (Group) group;
    }

    public void sendAlbumMsgUpd(Album oldAlbum, Album newAlbum) {
        MailSendLog mailSendLog = mailSendLogService.createMailSendLog(FormatUtil.getUUID32(), 0, MailConstants.MAIL_ROUTING_KEY_ALBUM, MailConstants.MAIL_EXCHANGE_NAME, new Date(), new Date(), MailConstants.ALBUM_UPD, newAlbum.getId(), -1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mailSendLog", mailSendLog);
        map.put("oldAlbum", oldAlbum);
        map.put("newAlbum", newAlbum);
        Group group = getGroup(oldAlbum);
        List<UserInfo> userInfos = getGroupMsgReceiver(group);
        map.put("users", userInfos);
        String content=String.format(albumUpdFormat,group.getGroupName(),oldAlbum.getAlbumName(),oldAlbum.getAlbumName(),newAlbum.getAlbumName());
        for(UserInfo each:userInfos){
            sendWebMsg(content,each.getUserName());
        }
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_ALBUM, map, new CorrelationData(mailSendLog.getMsgId()));
    }


}
