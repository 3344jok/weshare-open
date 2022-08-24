package com.zhang26.weshareserver.mapper;

import model.Group;
import model.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface UserGroupMapper {
    public int insert(UserGroup userGroup);
    public UserGroup getByUserIdAndGroupId(@Param("userId") int userId, @Param("groupId") int groupId);
    public List<UserGroup> getByGroupId(int groupId);
    public List<UserGroup> getByUserId(int userId);
    public int updateNoticeLevel(@Param("userGroup") UserGroup userGroup,@Param("noticeLevel") int noticeLevel);
    public int deleteByGroupId(int groupId);
}
