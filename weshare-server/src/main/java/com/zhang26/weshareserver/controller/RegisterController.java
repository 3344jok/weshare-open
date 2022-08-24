package com.zhang26.weshareserver.controller;


import com.zhang26.weshareserver.config.ReturnStatus;
import model.RegistrationForm;
import model.UserInfo;
import model.UserLogin;
import com.zhang26.weshareserver.service.MessageService;
import com.zhang26.weshareserver.service.UserInfoService;
import com.zhang26.weshareserver.service.UserLoginService;
import com.zhang26.weshareserver.util.FormatUtil;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MessageService messageService;

    @PostMapping
    public ReturnJsonUtil<Object> userRegistration(RegistrationForm form) {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        FormatUtil formatUtil=new FormatUtil();
        try {
            UserInfo userInfo= userInfoService.insertNewUserInfo(form.getUserName(),
                    form.getEmailAcount(),
                    formatUtil.MultipartFileToBytes(form.getProfilePhoto()),
                    formatUtil.MultipartFileToBytes(form.getBackground()),form.getPassword(),
                    form.getPfpType(),
                    form.getBgType()
            );
            messageService.sendRegisterMsg(userInfo);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        } catch (DuplicateKeyException e) {
            rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rj;
    }
    @PostMapping("/checkname")
    public ReturnJsonUtil<Object> checkRegisterName(String username){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        UserInfo userLogin= userInfoService.getByUserName(username);
        try {
            if(userLogin==null){
                rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
            }
            else{
                rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
            }
        }catch (Exception e ){
            rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
        }
        return rj;
    }

    @PostMapping("/checkemail")
    public ReturnJsonUtil<Object> checkRegisterEmail(String email){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        UserInfo userLogin= userInfoService.getByEmailAcount(email);
        try {
            if(userLogin==null){
                rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
            }
            else{
                rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
            }
        }catch (Exception e ){
            rj.setStatus(ReturnStatus.TRANSACTION_ERROR);
        }
        return rj;
    }


}
