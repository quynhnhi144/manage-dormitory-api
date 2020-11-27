package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.vehicle.VehicleBillDto;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleBillRepositoryCustom {
    int addVehicleBillRepository(VehicleBillDto vehicleBillDto);

    Optional<VehicleBillDto> getVehicleBillRecentlyByVehicleId(Integer vehicleId);

    Optional<VehicleBillDto> getVehicleBillRecentlyByStudentId(Integer studentId);
}
