package com.managedormitory.services;

import com.managedormitory.models.dao.Vehicle;
import com.managedormitory.models.dao.VehicleLeft;
import com.managedormitory.models.dto.pagination.PaginationVehicle;
import com.managedormitory.models.dto.vehicle.*;
import com.managedormitory.models.filter.VehicleFilter;
import org.hibernate.type.IntegerType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface VehicleService {
    List<Vehicle> getAllVehicles();

    List<VehicleDetailDto> getAllVehicleDto();

    List<VehicleLeft> getAllVehicleLeft();

    PaginationVehicle paginationGetAllVehicles(VehicleFilter vehicleFilter, int skip, int take);

    VehicleDetailDto getVehicleById(Integer id);

    VehicleDetailDto updateVehicle(Integer id, VehicleDetailDto vehicleDetailDto);

    VehicleMoveDto getInfoMovingVehicle(Integer vehicleId);

    VehicleBillDto VehicleBillInfoForNewVehicle(Integer studentId);

    ByteArrayInputStream exportExcel() throws IOException;

    int addVehicleLeft(VehicleMoveDto vehicleMoveDto);

    int addVehicle(VehicleNew vehicleNew);

    int countVehicle();
}
