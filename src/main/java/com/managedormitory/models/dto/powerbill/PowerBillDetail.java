package com.managedormitory.models.dto.powerbill;

import com.managedormitory.models.dto.room.DetailRoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerBillDetail {
    private DetailRoomDto detailRoomDto;
    private Integer billId;
    private Date startDate;
    private Date endDate;
    private long numberOfPowerBegin;
    private long numberOfPowerEnd;
    private long numberOfPowerUsed;
    private boolean isPay;
    private float priceAKWH;
    private float numberOfMoneyMustPay;
}
