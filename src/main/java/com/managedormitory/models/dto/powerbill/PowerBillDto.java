package com.managedormitory.models.dto.powerbill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerBillDto {
    private Integer roomId;
    private String roomName;
    private Integer billId;
    private Date startDate;
    private Date endDate;
    private long numberOfPowerBegin;
    private long numberOfPowerEnd;
    private long numberOfPowerUsed;
    private boolean isPay;
    private float priceAKWH;
}
