package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm implements Serializable {

    String userName;
    String emailAcount;
    MultipartFile profilePhoto;
    MultipartFile background;
    String password;

    String pfpType;

    String bgType;
}
