package com.managedormitory.models.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class VehicleFilter {
    private String campusName;
    private String typeVehicle;
    private String licensePlates;
}
