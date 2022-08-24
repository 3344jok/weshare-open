package com.zhang26.weshareserver.mapper;


import model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> getByPhotoId(int photoId);
    int insert(Comment comment);

    int deleteByCommentId(int commentId);
}
