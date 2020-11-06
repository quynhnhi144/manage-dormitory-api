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
    private String name;
    private Date birthday;
    private String phone;
    private String email;
    private String address;
    private Date startingDateOfStay;
    private Date endingDateOfStay;
    private RoomDto roomDto;
    private Boolean isPayRoom;
    private Boolean isPayWaterBill;
    private Boolean isPayVehicleBill;
    private Boolean isPayPowerBill;
}