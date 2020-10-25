package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationVehicle {
    private Map<String, List<VehicleDto>> data;
    private int total;
}
