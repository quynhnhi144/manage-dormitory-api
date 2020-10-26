package com.managedormitory.models.dto;

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
    private String roomName;
    private String campusName;
    private String typeRoom;
    private String userManager;
    private Boolean isPayRoom;
    private Boolean isPayWaterBill;
    private Boolean isPayVehicleBill;
    private Boolean isPayPowerBill;
}
