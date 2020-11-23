package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitchRoomHistoryDto {
    private Integer id;
    private String oldRoomName;
    private String newRoomName;
    private float givingRoomMoney;
    private float takingRoomMoney;
    private float givingWaterMoney;
    private float takingWaterMoney;
    private float givingVehicleMoney;
    private float takingVehicleMoney;
    private Integer studentId;
    private Date createDate;

}

