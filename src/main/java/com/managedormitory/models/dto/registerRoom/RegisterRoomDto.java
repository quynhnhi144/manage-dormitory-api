package com.managedormitory.models.dto.registerRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class RegisterRoomDto {
    private Integer id;

    private String idCard;

    private String studentName;

    private Date birthday;

    private String address;

    private String phone;

    private String email;

    private Date startingDateOfStay;

    private Integer roomId;

    private String roomName;

    private String typeRoomName;

    private String campusName;
}
