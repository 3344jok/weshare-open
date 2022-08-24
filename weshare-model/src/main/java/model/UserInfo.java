package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    int id;

    String userName;

    String emailAcount;

    byte[] profilePhoto;

    byte[] background;

    String pfpType;

    String bgType;

    int userLoginId;

}
