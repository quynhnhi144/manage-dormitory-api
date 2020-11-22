package com.managedormitory.controllers;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dto.VehicleBillDto;
import com.managedormitory.services.VehicleBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/vehicleBills")
public class VehicleBillController {
    @Autowired
    private VehicleBillService vehicleBillService;

    @PostMapping("/new-vehicle-bill")
    public int addVehicleBill(@RequestBody VehicleBillDto vehicleBillDto) {
        if (vehicleBillService.addVehicleBill(vehicleBillDto) > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot impelement method!!!");
    }
}
