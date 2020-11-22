package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.VehicleBillDto;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleBillRepositoryCustom {
    int addVehicleBillRepository(VehicleBillDto vehicleBillDto);
}
