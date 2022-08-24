package com.zhang26.weshareserver.config;

import com.zhang26.weshareserver.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Autowired
    private LogoutSuccessHandler accountLogoutSuccessHandler;

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    //配置加密方式
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置认证方式等
        auth.userDetailsService(userLoginService);// 将用户详情服务配置到 Spring Security 上

    }

//    Spring Security 默认的登入路径是/login 登出路径是/logout，向路径发送 post 请求传入用户名 username 和密
//    码 password 即可实现登入或登出。但 Spring Security 默认设置了 csrf 拦截，导致 post 请求默认被拦截，因此
//    码 password 即可实现登入或登出。但 Spring Security 默认设置了 csrf 拦截，导致 post 请求默认被拦截，因此
//    求。
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 相关的配置，包括登入登出、异常处理、会话管理等
        // 设置 csrf token，防止 post 请求拦截
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/register", "/login", "/logout","/photo","photo/**")
            .and()
                .authorizeRequests()
//                .antMatchers("/photo","/photo/**").permitAll()
                .antMatchers("/register", "/register/**").permitAll()
                .antMatchers("/**").hasRole("USER")  // 使用 /api/** 而不是 /api/  ** 为通配符
//                .antMatchers("/photo","/photo/**").permitAll()
//                .antMatchers("/**").authenticated()
            .and()
                .formLogin()  // 配置登入
                .loginProcessingUrl("/login")  // 设置登录接口路径，登录方式为 post 请求，字段为用户名 username 及密码 password
                .successHandler(authSuccessHandler)  // 注入成功处理器
                .failureHandler(authFailureHandler)  // 注入失败处理器
                .permitAll()  // 允许所有用户访问（包括匿名用户
            .and()
                .logout()
                .logoutSuccessHandler(accountLogoutSuccessHandler)  // 注入登出处理器
                .deleteCookies("JSESSIONID")  // 登出时删除 JSESSIONID
            .and()
                .exceptionHandling()  // 异常处理
                .authenticationEntryPoint(authEntryPoint);

    }



}
