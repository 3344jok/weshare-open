package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.CommentMapper;
import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @CacheEvict(cacheNames="weshare_comment", allEntries=true)
    public int deleteByCommentId(int commentId){
        return commentMapper.deleteByCommentId(commentId);
    }
    @CacheEvict(cacheNames="weshare_comment", allEntries=true)
    public int insert(Comment comment){return commentMapper.insert(comment);}
    @Cacheable(cacheNames = "weshare_comment",keyGenerator = "cacheKeyGenerator")
    public List<Comment> getByPhotoId(int photoId){return commentMapper.getByPhotoId(photoId);}
}
