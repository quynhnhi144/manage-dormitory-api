package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DurationBetweenTwoRoom {
    private Integer oldRoomId;
    private Integer newRoomId;

    private String oldRoomName;
    private String newRoomName;

    private LocalDate roomStartDate;
    private LocalDate roomEndDate;
    private float durationRoomMoney;

    private LocalDate waterStartDate;
    private LocalDate waterEndDate;
    private float durationWaterMoney;

    private LocalDate vehicleStartDate;
    private LocalDate vehicleEndDate;
    private float durationVehicleMoney;
}
