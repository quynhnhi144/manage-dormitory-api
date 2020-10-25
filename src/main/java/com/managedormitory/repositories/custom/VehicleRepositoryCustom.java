package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.VehicleDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepositoryCustom {
    List<VehicleDto> getAllVehicleByTime();
}
