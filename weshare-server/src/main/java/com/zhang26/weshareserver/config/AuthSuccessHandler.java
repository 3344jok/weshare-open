package com.zhang26.weshareserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//权限认证成功处理器
@Component  // 注册 Spring 上下文
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("text/json;charset=utf-8");  // 设置返回数据格式为 json
        ObjectMapper objectMapper = new ObjectMapper();
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>(ReturnStatus.SUCCESS);
        objectMapper.writeValue(httpServletResponse.getWriter(), rj);
    }
}


