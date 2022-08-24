package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.UserInfoMapper;
import com.zhang26.weshareserver.mapper.UserLoginMapper;
import model.UserInfo;
import model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Blob;

@Service
public class UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    MessageService messageService;

    public UserInfo getByUserId(int userId){
        return userInfoMapper.getById(userId);
    }

    public UserInfo getByUserName(String name){
        return userInfoMapper.getByUserName(name);
    }

    public UserInfo getByEmailAcount(String emailAcount){
        return userInfoMapper.getByEmailAcount(emailAcount);
    }

    public UserInfo getByUserLogin(UserLogin userLogin){
        return userInfoMapper.getByUserLoginId(userLogin.getId());
    }

    public UserInfo getByUserLoginId(int id){
        return userInfoMapper.getByUserLoginId(id);
    }


    public UserInfo getById(int id){
        return userInfoMapper.getById(id);
    }
    public UserInfo update(UserInfo userInfo, String name, String emailAcount, byte[] profilePhoto,byte[] background,String pfpType,String bgType){
        UserLogin userLogin=userLoginService.getById(userInfo.getUserLoginId());
        userLoginService.update(userLogin,name, userLogin.getPassword(), emailAcount);
        userInfo.setUserName(name);
        userInfo.setEmailAcount(emailAcount);
        userInfo.setProfilePhoto(profilePhoto);
        userInfo.setBackground(background);
        userInfo.setBgType(bgType);
        userInfo.setPfpType(pfpType);
        userInfoMapper.update(userInfo);
        return userInfo;
    }
    public UserInfo insertNewUserInfo(String name,String email,byte[] proP,byte[] bg,String password,String pfpType,String bgType){
        UserLogin userLogin=userLoginService.insertNewUserLogin(name,password,email);
        UserInfo userInfo=new UserInfo();
        userInfo.setUserName(name);
        userInfo.setEmailAcount(email);
        userInfo.setProfilePhoto(proP);
        userInfo.setBackground(bg);
        userInfo.setUserLoginId(userLogin.getId());
        userInfo.setBgType(bgType);
        userInfo.setPfpType(pfpType);
        userInfoMapper.insertNewUserInfo(userInfo);
        return userInfo;
    }
}
