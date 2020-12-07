package com.managedormitory.models.dto.student;

import com.managedormitory.models.dto.room.RoomDto;
import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDetailDto {
    private Integer id;
    private String idCard;
    private String name;
    private Date birthday;
    private String phone;
    private String email;
    private String address;
    private Date startingDateOfStay;
    private RoomDto roomDto;
    private Boolean isPayRoom;
    private Boolean isPayWaterBill;
    private boolean isActive;
    private Integer waterPriceId;
    private Integer vehicleId;
}
