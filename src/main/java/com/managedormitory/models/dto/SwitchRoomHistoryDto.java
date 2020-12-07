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
    private String studentIdCard;
    private String oldRoomName;
    private String newRoomName;
    private float roomMoney;
    private float waterMoney;
    private float vehicleMoney;
    private Integer studentId;
    private Date createDate;

}

