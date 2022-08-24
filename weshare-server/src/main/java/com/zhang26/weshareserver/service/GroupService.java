package com.zhang26.weshareserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang26.weshareserver.mapper.GroupMapper;
import com.zhang26.weshareserver.mapper.UserGroupMapper;
import model.Album;
import model.Group;
import model.UserGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    GroupMapper groupMapper;

//    @Cacheable(cacheNames = "weshare_group",keyGenerator = "cacheKeyGenerator")
    public List<Group> getByAllUserGroup(List<UserGroup> list,int userId, String pattern){
        return groupMapper.getByUserIdAll(userId,pattern);
    }

//    @Cacheable(cacheNames = "weshare_group",keyGenerator = "cacheKeyGenerator")
    public List<Group> getByMineUserGroup(List<UserGroup> list,int userId,String pattern){
        return groupMapper.getByUserIdOnlyMine(userId,pattern);
    }

//    @CacheEvict(cacheNames="weshare_group", allEntries=true)
    public int deleteByGroupId(int groupId){
        return groupMapper.deleteById(groupId);
    }

    public List<Group> searchByPattern(String pattern){
        return groupMapper.searchByGroupName(pattern);
    }

//    @CacheEvict(cacheNames="weshare_group", allEntries=true)
    public int addGroup(Group group){
        return groupMapper.insert(group);
    }

//    @Cacheable(cacheNames = "weshare_group",keyGenerator = "cacheKeyGenerator")
    public Group getByUuid(String uuid){
        return groupMapper.getByUuid(uuid);
    };

//    @Cacheable(cacheNames = "weshare_group",keyGenerator = "cacheKeyGenerator")
    public Group getByGroupId(int groupId){
        return groupMapper.getById(groupId);
    };

//    @CacheEvict(cacheNames="weshare_group", allEntries=true)
    public int update(Group group){
        return groupMapper.update(group);
    }

//    @Cacheable(cacheNames = "weshare_album",keyGenerator = "cacheKeyGenerator")
    public List<Album> getAlbums(int groupId,int userId,String pattern,Boolean all){
        return groupMapper.getAlbums(groupId, userId,pattern,all);
    }
}
