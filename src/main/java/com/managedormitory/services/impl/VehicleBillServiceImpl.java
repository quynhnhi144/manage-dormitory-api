package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dto.VehicleBillDto;
import com.managedormitory.services.VehicleBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleBillServiceImpl implements VehicleBillService {
    @Autowired
    private VehicleBillService vehicleBillService;

    @Override
    public int addVehicleBill(VehicleBillDto vehicleBillDto) {
        if (vehicleBillService.addVehicleBill(vehicleBillDto) > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot implement method!!!");
    }
}
