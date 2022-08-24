package com.zhang26.weshareserver.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang26.weshareserver.config.ReturnStatus;
import model.Album;
import model.Group;
import model.GroupAlbum;
import model.UserGroup;
import com.zhang26.weshareserver.service.AlbumService;
import com.zhang26.weshareserver.service.GroupAlbumService;
import com.zhang26.weshareserver.service.GroupService;
import com.zhang26.weshareserver.service.MessageService;
import com.zhang26.weshareserver.util.DateUtil;
import com.zhang26.weshareserver.util.FormatUtil;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    GroupService groupService;
    @Autowired
    GroupAlbumService groupAlbumService;
    @Autowired
    AlbumService albumService;

    @Autowired
    MessageService messageService;
    @GetMapping("/table")
    public ReturnJsonUtil<Object> getGroupTable(boolean all, int groupId, int userId,int pageIndex, int pageSize, String pattern){
        int pageNum=pageIndex;
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        Boolean allClass=null;
        if(all==true){
            allClass=new Boolean("true");
        }
        else{
            allClass=new Boolean("false");
        }
        try {
            PageHelper.startPage(pageNum,pageSize);
            List<Album> table = groupService.getAlbums(groupId, userId, pattern, allClass);
            PageInfo<Album> pageInfo=new PageInfo<>(table);
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
    public ReturnJsonUtil<Object> deleteByGroupId(int albumId){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try{
            Album album=albumService.getByAlbumId(albumId);
            messageService.sendAlbumMsgDel(album);
            groupAlbumService.deleteByAlbumId(albumId);
            albumService.deleteByAlbumId(albumId);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("删除失败");
        }
        return rj;
    }

    @PostMapping("/add")
    public ReturnJsonUtil<Object> addGroup(String albumName, int userId, MultipartFile cover,String ctype, String albumRemark,int groupId){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try{
            Album album=new Album();
            album.setAlbumName(albumName);
            album.setCreatorId(userId);
            album.setCover(FormatUtil.MultipartFileToBytes(cover));
            album.setCtype(ctype);
            album.setRemark(albumRemark);
            album.setDate(DateUtil.utilDateToSQLDate(new Date()));
            albumService.addAlbum(album);
            GroupAlbum groupAlbum=new GroupAlbum();
            groupAlbum.setGroupId(groupId);
            groupAlbum.setAlbumId(album.getId());
            groupAlbumService.insert(groupAlbum);
            Group group=groupService.getByGroupId(groupAlbum.getGroupId());
            messageService.sendGroupMsgAdd(group,album);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("添加失败");
        }
        return rj;
    }

    @PostMapping("/update")
    public ReturnJsonUtil<Object> update(String albumName, int albumId, MultipartFile cover,String ctype, String remark){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try{
            Album album=albumService.getByAlbumId(albumId);
            Album oldAlbum=albumService.getByAlbumId(albumId);
            if(albumName!=null){
                album.setAlbumName(albumName);
            }
            if(cover!=null){
                album.setCover(FormatUtil.MultipartFileToBytes(cover));
                album.setCtype(ctype);
            }
            if(remark!=null){
                album.setRemark(remark);
            }
//            messageService.sendAlbumMsgUpd(oldAlbum,album);
            albumService.update(album);
            rj.setData(album);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("修改失败");
        }
        return rj;


    }

}
