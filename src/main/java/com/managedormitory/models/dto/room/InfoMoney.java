package com.managedormitory.models.dto.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoMoney {
    private Integer roomId;
    private String roomName;
    private Date maxDateRoomBill;
    private Date maxDateWaterBill;
    private Date maxDateVehicleBill;
    private Integer waterPriceId;
    private Float roomPrice;
    private Float waterPrice;
    private Integer maxQuantityStudent;
}
