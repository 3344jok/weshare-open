package com.zhang26.weshareserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccountLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException, IOException {
        httpServletResponse.setContentType("text/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        ReturnJsonUtil<Object> rj;
        if (authentication != null) {
            rj = new ReturnJsonUtil<>(ReturnStatus.SUCCESS);
        } else {
            rj = new ReturnJsonUtil<>(ReturnStatus.USER_NOT_LOGIN);
        }
        objectMapper.writeValue(httpServletResponse.getWriter(), rj);
    }
}