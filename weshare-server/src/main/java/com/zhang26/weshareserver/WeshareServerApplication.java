package com.zhang26.weshareserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan("com.zhang26.weshareserver.mapper")
public class WeshareServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeshareServerApplication.class, args);
    }

}
