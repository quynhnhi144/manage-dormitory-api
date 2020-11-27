package com.managedormitory.models.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleNew {
    private String licensePlates;
    private Integer typeVehicleId;
    private Integer vehiclePriceId;
    private  VehicleBillDto vehicleBillDto;
}
