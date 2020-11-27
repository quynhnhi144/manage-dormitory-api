package com.managedormitory.models.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleDto {
    private Integer id;
    private String licensePlates;
    private Integer typeVehicleId;
    private String typeVehicle;
    private Integer studentId;
    private String studentName;
    private String roomName;
    private String campusName;
    private String userManager;
    private Date startDate;
    private Date endDate;
    private Integer vehiclePriceId;
}
