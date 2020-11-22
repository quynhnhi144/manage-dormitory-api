package com.managedormitory.models.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPriceAndWaterPriceDto {
    private Integer roomId;
    private LocalDate roomStartDate;
    private LocalDate roomEndDate;
    private float moneyOfRoomMustPay;

    private Integer waterPriceId;
    private LocalDate waterStartDate;
    private LocalDate waterEndDate;
    private float moneyOfWaterMustPay;
}
