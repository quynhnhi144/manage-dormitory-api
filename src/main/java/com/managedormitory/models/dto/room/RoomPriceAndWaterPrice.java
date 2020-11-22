package com.managedormitory.models.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPriceAndWaterPrice {
    private Integer roomId;
    private Date maxDateRoomBill;
    private Date maxDateWaterBill;
    private Integer waterPriceId;
    private float roomPrice;
    private float waterPrice;
    private Integer maxQuantityStudent;
}
