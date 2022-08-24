package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album implements Serializable {

    int id;
    String albumName;
    int creatorId;
    byte[] cover;
    Date date;
    String remark;
    String ctype;
    List<Photo> photos;
}
