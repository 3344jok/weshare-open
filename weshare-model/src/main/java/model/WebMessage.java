package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebMessage {

    int id;
    String userName;
    String content;
    int status;
    Date createDate;
}
