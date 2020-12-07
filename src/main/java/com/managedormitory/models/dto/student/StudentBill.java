package com.managedormitory.models.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentBill {
    private Integer studentId;
    private String studentName;
    private Integer roomId;
    private String roomName;
    private String studentIdCard;

    private LocalDate roomStartDate;
    private LocalDate roomEndDate;
    private float moneyOfRoomMustPay;
    private Integer maxQuantityStudent;

    private LocalDate waterStartDate;
    private LocalDate waterEndDate;
    private float moneyOfWaterMustPay;

    private Integer vehicleId;
    private LocalDate vehicleStartDate;
    private LocalDate vehicleEndDate;
    private float moneyOfVehicleMustPay;
}
