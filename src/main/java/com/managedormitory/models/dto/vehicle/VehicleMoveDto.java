package com.managedormitory.models.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleMoveDto {
    private Integer id;
    private String licensePlates;
    private String studentName;
    private LocalDate leavingDate;
    private float numberOfVehicleMoney;
    private Integer studentId;
}
