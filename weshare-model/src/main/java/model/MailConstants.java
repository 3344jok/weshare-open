package model;

public class MailConstants {
    public static final Integer DELIVERING = 0;//消息投递中
    public static final Integer SUCCESS = 1;//消息投递成功
    public static final Integer FAILURE = 2;//消息投递失败

    public static final Integer MAX_TRY_COUNT = 3;//最大重试次数
    public static final Integer MSG_TIMEOUT = 1;//消息超时时间

    public static final Integer REGISTER = 1;
    public static final Integer GROUP_ADD = 2;
    public static final Integer GROUP_DEL = 3;
    public static final Integer GROUP_UPD = 4;
    public static final Integer GROUP_JOIN = 5;
    public static final Integer ALBUM_DEL = 6;
    public static final Integer ALBUM_UPD = 7;

    public static final String MAIL_QUEUE_REGISTER = "weshare.mail.queue.register";
    public static final String MAIL_QUEUE_GROUP = "weshare.mail.queue.group";
    public static final String MAIL_QUEUE_ALBUM = "weshare.mail.queue.album";
    public static final String MAIL_EXCHANGE_NAME = "weshare.mail.exchange";
    public static final String MAIL_ROUTING_KEY_REGISTER = "weshare.mail.register";
    public static final String MAIL_ROUTING_KEY_GROUP = "weshare.mail.group";
    public static final String MAIL_ROUTING_KEY_ALBUM = "weshare.mail.album";
}