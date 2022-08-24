package com.zhang26.weshareserver.mapper;

import model.UserLogin;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserLoginMapper {

    int insertNewUserLogin(UserLogin user);

    UserLogin getByUserName(String name);

    UserLogin getById(int id);

    int getIdByUserName(String name);

    int update(UserLogin userLogin);

}
