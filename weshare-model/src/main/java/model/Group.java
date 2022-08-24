package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {
    int id;
    String groupName;
    int creatorId;
    byte[] cover;
    String invitationCode;
    Timestamp date;
    String remark;
    String groupUuid;
    String ctype;
    List<Album> albums;

}
