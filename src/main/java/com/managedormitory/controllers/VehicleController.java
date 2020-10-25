package com.managedormitory.controllers;

import com.managedormitory.models.dto.PaginationVehicle;
import com.managedormitory.models.dto.VehicleDto;
import com.managedormitory.models.filter.VehicleFilter;
import com.managedormitory.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public PaginationVehicle filterVehicle(@RequestParam(required = false) String campusName, @RequestParam(required = false) String searchText, @RequestParam(required = false) String typeVehicle, int skip, int take) {
        VehicleFilter vehicleFilter = VehicleFilter.builder().campusName(campusName).licensePlates(searchText).typeVehicle(typeVehicle).build();
        return vehicleService.paginationGetAllVehicles(vehicleFilter, skip, take);
    }

    @GetMapping("/{id}")
    public VehicleDto getDetailAVehicle(@PathVariable Integer id) {
        try {
            return vehicleService.getVehicleById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
