package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    int id;
    int senderId;
    Integer receiverId;
    String senderName;
    String recevierName;
    String content;
    Date date;
    int photoId;
}
