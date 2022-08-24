package com.zhang26.weshareserver.service;

import com.github.pagehelper.PageHelper;
import com.zhang26.weshareserver.mapper.UserGroupMapper;
import model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupService {

    @Autowired
    UserGroupMapper userGroupMapper;

//    @Cacheable(cacheNames = "weshare_usergroup",keyGenerator = "cacheKeyGenerator")
    public List<UserGroup> getByUserId(int id){
        return userGroupMapper.getByUserId(id);
    }

//    @Cacheable(cacheNames = "weshare_usergroup",keyGenerator = "cacheKeyGenerator")
    public List<UserGroup> getByGroupId(int id){
        return userGroupMapper.getByGroupId(id);
    }


//    @Cacheable(cacheNames = "weshare_usergroup",keyGenerator = "cacheKeyGenerator")
    public List<UserGroup> getByUserIdWithPage(int id,int pageNum,int pageSize){
        List<UserGroup> ret=userGroupMapper.getByUserId(id);
        return ret;
    }

//    @CacheEvict(cacheNames="weshare_usergroup", allEntries=true)
    public int deleteByGroupId(int groupId){
        return userGroupMapper.deleteByGroupId(groupId);
    }
//    @CacheEvict(cacheNames="weshare_usergroup", allEntries=true)
    public int insert(UserGroup userGroup){return userGroupMapper.insert(userGroup);}
}
