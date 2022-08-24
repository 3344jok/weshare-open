package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.UserLoginMapper;
import model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService implements UserDetailsService {

    @Autowired
    private UserLoginMapper userLoginMapper;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userLoginMapper.getByUserName(s);
    }

    public UserLogin update(UserLogin oldUserLogin, String userName, String password, String emailAcount){
        oldUserLogin.setUserName(userName);
        oldUserLogin.setPassword(password);
        oldUserLogin.setEmailAcount(emailAcount);
        userLoginMapper.update(oldUserLogin);
        return oldUserLogin;
    }

    public UserLogin insertNewUserLogin(String name,String password,String emailAcount){
        UserLogin userLogin=new UserLogin();
        userLogin.setUserName(name);
        userLogin.setPassword(password);
        userLogin.setEmailAcount(emailAcount);
        userLoginMapper.insertNewUserLogin(userLogin);
        return userLogin;
    }

    public UserLogin getById(int id){
        return userLoginMapper.getById(id);
    }

}
