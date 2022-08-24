package com.zhang26.weshareserver.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang26.weshareserver.config.ReturnStatus;
import model.Group;
import model.UserGroup;
import model.UserInfo;
import com.zhang26.weshareserver.service.GroupService;
import com.zhang26.weshareserver.service.MessageService;
import com.zhang26.weshareserver.service.UserGroupService;
import com.zhang26.weshareserver.service.UserInfoService;
import com.zhang26.weshareserver.util.FormatUtil;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    UserGroupService userGroupService;

    @Autowired
    GroupService groupService;
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    MessageService messageService;

    @GetMapping("/table")
    public ReturnJsonUtil<Object> getGroupTable(boolean all,int id,int pageIndex,int pageSize,String pattern){
        int pageNum=pageIndex;
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        List<UserGroup> userGroups=null;
        try{
            userGroups=userGroupService.getByUserIdWithPage(id,pageNum,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            log.error("找不到user和group的映射关系！");
        }
        List<Group> table=null;
        try {
            PageHelper.startPage(pageNum,pageSize);
            if(all){
                table=groupService.getByAllUserGroup(userGroups,id,pattern);
            }
            else{
                table=groupService.getByMineUserGroup(userGroups,id,pattern);
            }
            PageInfo<Group> pageInfo=new PageInfo<>(table);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
            HashMap<String,Object> data=new HashMap<>();
            data.put("list",table);
            data.put("pageTotal",pageInfo.getTotal());
            rj.setData(data);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            log.error("找不到group table！");
            e.printStackTrace();
        }
        return rj;
    }

    @DeleteMapping
    public ReturnJsonUtil<Object> deleteByGroupId(int groupId){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try{
            Group group=groupService.getByGroupId(groupId);
            messageService.sendGroupMsgDel(group);
            userGroupService.deleteByGroupId(groupId);
            groupService.deleteByGroupId(groupId);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("删除失败");
        }
        return rj;
    }

    @PostMapping("/add")
    public ReturnJsonUtil<Object> addGroup(String groupName, int userId, MultipartFile cover,String invitationCode,String ctype,String remark){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try{
            Group group=new Group();
            group.setGroupName(groupName);
            group.setCreatorId(userId);
            group.setCover(FormatUtil.MultipartFileToBytes(cover));
            group.setInvitationCode(invitationCode);
            group.setDate(new Timestamp(System.currentTimeMillis()));
            group.setRemark(remark);
            group.setGroupUuid(FormatUtil.getUUID32());
            group.setCtype(ctype);
            groupService.addGroup(group);
            UserGroup userGroup=new UserGroup();
            userGroup.setUserId(userId);
            userGroup.setGroupId(group.getId());
            userGroupService.insert(userGroup);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("添加失败");
        }
        return rj;
    }

    @PostMapping("/join")
    public ReturnJsonUtil<Object> joinGroup(String targetUuid,String targetInvitationCode,int userId){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        UserGroup userGroup = null;
        try{
            Group group=groupService.getByUuid(targetUuid);
            if(group==null){
                rj.setStatus(ReturnStatus.COMMON_ERROR);
                rj.setStatusMsg("共享ID输入错误");
            }
            else{
                if(group.getInvitationCode().equals(targetInvitationCode)){
                    userGroup=new UserGroup();
                    userGroup.setUserId(userId);
                    userGroup.setGroupId(group.getId());
                    userGroup.setNoticeLevel(0);
                    userGroupService.insert(userGroup);
                    UserInfo userInfo=userInfoService.getByUserId(userId);
                    messageService.sendGroupMsgJoin(group,userInfo);
                }
                else {
                    rj.setStatus(ReturnStatus.COMMON_ERROR);
                    rj.setStatusMsg("邀请码输入错误");
                }
            }
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("加入失败");
        }
        return rj;
    }

    @PostMapping("/update")
    public ReturnJsonUtil<Object> update(int groupId,String groupName,MultipartFile cover,String ctype,String remark,String invitationCode){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try{
            Group group=groupService.getByGroupId(groupId);
            Group oldGroup=groupService.getByGroupId(groupId);
            if(groupName!=null){
                group.setGroupName(groupName);
            }
            if(invitationCode!=null){
                group.setInvitationCode(invitationCode);
            }
            if(cover!=null){
                group.setCover(FormatUtil.MultipartFileToBytes(cover));
            }
            if(ctype!=null){
                group.setCtype(ctype);
            }
            if(remark!=null){
                group.setRemark(remark);
            }
            groupService.update(group);
            messageService.sendGroupMsgUpd(oldGroup,group);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
            rj.setData(group);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("更新失败");
        }
        return rj;
    }
    @GetMapping("/getCreatorName")
    public ReturnJsonUtil<Object> getCreatorName(int creatorId) {
        ReturnJsonUtil<Object> rj=new ReturnJsonUtil<>();
        try{
            UserInfo userInfo=userInfoService.getByUserId(creatorId);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
            rj.setData(userInfo.getUserName());
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("找不到作者名称");
        }
        return rj;
    }
    @GetMapping("getgroupdetails")
    public ReturnJsonUtil<Object> getGroupDetails(int groupId){
        ReturnJsonUtil<Object> rj=new ReturnJsonUtil<>();
        try{
            Group group = groupService.getByGroupId(groupId);
            System.out.println(group);
            rj.setData(group);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);

        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("找不到共享好友");
        }
        return rj;
    }

}
