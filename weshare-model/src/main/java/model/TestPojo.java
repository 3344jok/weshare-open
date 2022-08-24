package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestPojo implements Serializable {
//    "id": 3,
//    "name": "王五",
//    "money": 789,
//    "address": "湖南省长沙市",
//    "state": "失败",
//    "date": "2019-11-11",
//    "thumb": "https://lin-xin.gitee.io/images/post/parcel.png"
    int id;

    String name;

    int money;

    String address;

    String state;

    Date date;

    String thumb;

}
