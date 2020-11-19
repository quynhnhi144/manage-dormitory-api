package com.managedormitory.models.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentMoveDto {
    private Integer id;
    private LocalDate leavingDate;
    private float numberOfRoomMoney;
    private float numberOfWaterMoney;
    private float numberOfVehicleMoney;
    private Integer roomId;
}
