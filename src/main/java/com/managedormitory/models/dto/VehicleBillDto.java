package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleBillDto {
    private Integer billId;
    private String studentName;
    private Integer studentId;
    private Date startDate;
    private Date endDate;
    private float price;
    private Integer roomId;
}