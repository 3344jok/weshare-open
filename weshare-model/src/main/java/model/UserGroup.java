package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup implements Serializable {
    int id;
    int userId;
    int groupId;
    int noticeLevel;
}
