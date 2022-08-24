package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo implements Serializable {
    int id;
    int creatorId;
    Date date;
    String filePath;
    int photoStatus;
    int albumId;
}
