package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.vehicle.VehicleDetailDto;
import com.managedormitory.models.dto.vehicle.VehicleDto;
import com.managedormitory.models.dto.vehicle.VehicleMoveDto;
import com.managedormitory.models.dto.vehicle.VehicleNew;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepositoryCustom {
    List<VehicleDto> getAllVehicleByTime();

    int addVehicle(VehicleNew vehicleNew);

    int updateVehicle(Integer id, VehicleDetailDto vehicleDetailDto);

    int addVehicleLeft(VehicleMoveDto vehicleMoveDto);
}
