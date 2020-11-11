package com.managedormitory.services;

import com.managedormitory.models.dao.Vehicle;
import com.managedormitory.models.dto.*;
import com.managedormitory.models.dto.pagination.PaginationVehicle;
import com.managedormitory.models.filter.VehicleFilter;

import java.util.List;

public interface VehicleService {
    List<Vehicle> getAllVehicles();
    List<VehicleDetailDto> getAllVehicleDto();
    PaginationVehicle paginationGetAllVehicles(VehicleFilter vehicleFilter, int skip, int take);
    VehicleDetailDto getVehicleById(Integer id);
    int countVehicle();
}
