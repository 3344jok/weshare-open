package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailSendLog implements Serializable {

    String msgId;
    int status;
    String routeKey;
    String exchange;
    int count;
    Date tryTime;
    Date createTime;
    Date updateTime;
    int type;
    int parentId;
    int childId;
}
