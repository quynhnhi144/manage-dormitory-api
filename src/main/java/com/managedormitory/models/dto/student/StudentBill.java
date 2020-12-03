package com.managedormitory.models.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentBill {
    private String studentIdCard;
    private String studentName;
    private String roomName;
    private Float numberOfRoomMoney;
    private Float numberOfWaterMoney;
    private Float numberOfVehicleMoney;
}
