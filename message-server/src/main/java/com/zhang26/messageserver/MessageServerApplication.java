package com.zhang26.messageserver;


import model.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.zhang26"})
public class MessageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }

    @Bean
    Queue queueRegister() {
        return new Queue(MailConstants.MAIL_QUEUE_REGISTER);
    }

    @Bean
    Queue queueGroup() {
        return new Queue(MailConstants.MAIL_QUEUE_GROUP);
    }
    @Bean
    Queue queueAlbum() {
        return new Queue(MailConstants.MAIL_QUEUE_ALBUM);
    }
}
