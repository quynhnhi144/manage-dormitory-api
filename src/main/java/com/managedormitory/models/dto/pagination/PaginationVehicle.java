package com.managedormitory.models.dto.pagination;

import com.managedormitory.models.dto.VehicleDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationVehicle {
    private Map<String, List<VehicleDetailDto>> data;
    private int total;
}
