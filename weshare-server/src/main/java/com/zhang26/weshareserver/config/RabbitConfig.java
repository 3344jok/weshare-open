package com.zhang26.weshareserver.config;

import model.MailConstants;
import com.zhang26.weshareserver.service.MailSendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public final static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
    @Autowired
    CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    MailSendLogService mailSendLogService;

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = data.getId();
            if (ack) {
                logger.info(msgId + ":消息发送成功");
                mailSendLogService.updateMailSendLogStatus(msgId, 1);//修改数据库中的记录，消息投递成功
            } else {
                logger.info(msgId + ":消息发送失败");
            }
        });
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingkey) -> {
            logger.info("消息发送失败");
        });
        return rabbitTemplate;
    }

    @Bean
    Queue mailRegisterQueue() {
        return new Queue(MailConstants.MAIL_QUEUE_REGISTER, true);
    }
    @Bean
    Queue mailGroupQueue() {
        return new Queue(MailConstants.MAIL_QUEUE_GROUP, true);
    }
    @Bean
    Queue mailAlbumQueue() {
        return new Queue(MailConstants.MAIL_QUEUE_ALBUM, true);
    }

    @Bean
    DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding mailRegisterBinding() {
        return BindingBuilder.bind(mailRegisterQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY_REGISTER);
    }

    @Bean
    Binding mailGroupBinding() {
        return BindingBuilder.bind(mailGroupQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY_GROUP);
    }

    @Bean
    Binding mailAlbumBinding() {
        return BindingBuilder.bind(mailAlbumQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY_ALBUM);
    }

}