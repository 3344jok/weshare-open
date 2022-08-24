package com.zhang26.weshareserver.controller;

import com.zhang26.weshareserver.config.ReturnStatus;
import model.Comment;
import com.zhang26.weshareserver.service.CommentService;
import com.zhang26.weshareserver.util.DateUtil;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @GetMapping("/getcomments")
    public ReturnJsonUtil<Object> getComments(int photoId){
        ReturnJsonUtil<Object> rj=new ReturnJsonUtil<>();
        try{
            List<Comment> list=commentService.getByPhotoId(photoId);
            rj.setData(list);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("查找评论失败");
        }
        return rj;
    }

    @GetMapping("/add")
    public ReturnJsonUtil<Object> add(int senderId,int receiverId,int photoId,String content){
        ReturnJsonUtil<Object> rj=new ReturnJsonUtil<>();
        try{
            Comment comment=new Comment();
            comment.setSenderId(senderId);
            if(receiverId!=-1){
                comment.setReceiverId(receiverId);
            }
            comment.setDate(DateUtil.utilDateToSQLDate(new Date()));
            comment.setContent(content);
            comment.setPhotoId(photoId);
            commentService.insert(comment);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
            rj.setStatusMsg("评论成功");
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("插入评论失败");
        }
        return rj;
    }
    @DeleteMapping
    public ReturnJsonUtil<Object> delete(int commentId){
        ReturnJsonUtil<Object> rj=new ReturnJsonUtil<>();
        try {
            commentService.deleteByCommentId(commentId);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("删除评论失败");
        }
        return rj;
    }
}
