package com.zhang26.weshareserver;

import com.zhang26.weshareserver.mapper.*;
import model.*;
import com.zhang26.weshareserver.service.*;
import com.zhang26.weshareserver.util.DateUtil;
import com.zhang26.weshareserver.util.FormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class WeshareServerApplicationTest {

    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    DataSource dataSource;
    @Autowired
    UserLoginService userLoginService;
    @Autowired
    UserGroupMapper userGroupMapper;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    GroupAlbumMapper groupAlbumMapper;

    @Autowired
    PhotoMapper photoMapper;
    @Autowired
    AlbumMapper albumMapper;

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    PhotoService photoService;
    @Autowired
    MailSendLogMapper mailSendLogMapper;

    @Autowired
    MailSendLogService mailSendLogService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MessageService messageService;

    @Autowired
    GroupService groupService;

    @Autowired
    WebMessageMapper webMessageMapper;

    @Autowired
    WebMessageService webMessageService;
    @Test
    void contextLoads() throws SQLException {

    }
}