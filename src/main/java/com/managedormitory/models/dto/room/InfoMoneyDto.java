package com.managedormitory.models.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoMoneyDto {
    private Integer roomId;
    private String roomName;
    private LocalDate roomStartDate;
    private LocalDate roomEndDate;
    private float moneyOfRoomMustPay;

    private Integer waterPriceId;
    private LocalDate waterStartDate;
    private LocalDate waterEndDate;
    private float moneyOfWaterMustPay;

    private LocalDate vehicleStartDate;
    private LocalDate vehicleEndDate;
    private float moneyOfVehicleMustPay;
    private Integer maxQuantityStudent;
}
