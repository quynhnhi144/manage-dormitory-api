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
    private String idCard;
    private String name;
    private Date birthday;
    private String phone;
    private String email;
    private String address;
    private Date startingDateOfStay;
    private Integer roomId;
    private Integer waterPriceId;
    private Integer vehicleId;
}
