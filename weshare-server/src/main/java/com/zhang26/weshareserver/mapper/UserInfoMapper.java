package com.zhang26.weshareserver.mapper;

import model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

    UserInfo getByUserName(String name);

    UserInfo getByEmailAcount(String emailAcount);

    UserInfo getByUserLoginId(int id);

    UserInfo getById(int id);

    int update(UserInfo userInfo);

    int insertNewUserInfo(UserInfo userInfo);


}
