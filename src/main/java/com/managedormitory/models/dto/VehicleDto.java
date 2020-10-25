package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleDto {
    private Integer id;
    private String licensePlates;
    private String typeVehicle;
    private Integer studentId;
    private String studentName;
    private String roomName;
    private String campusName;
    private String userManager;
    private Boolean isPayVehicleBill;
}
