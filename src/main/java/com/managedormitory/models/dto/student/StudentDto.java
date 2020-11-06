package com.managedormitory.models.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDto {
    private Integer id;
    private String name;
    private Date birthday;
    private String phone;
    private String email;
    private String address;
    private Date startingDateOfStay;
    private Date endingDateOfStay;
    private Integer roomId;
    private Boolean isPayRoom;
    private Boolean isPayWaterBill;
    private Boolean isPayVehicleBill;
    private Boolean isPayPowerBill;
}