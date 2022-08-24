package com.zhang26.weshareserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException, IOException {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>(ReturnStatus.USER_NOT_LOGIN);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), rj);
    }
}