package com.managedormitory.controllers;

import com.managedormitory.models.dto.VehicleDetailDto;
import com.managedormitory.models.dto.pagination.PaginationVehicle;
import com.managedormitory.models.dto.VehicleDto;
import com.managedormitory.models.filter.VehicleFilter;
import com.managedormitory.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public List<VehicleDetailDto> getAllVehicles() {
        return vehicleService.getAllVehicleDto();
    }

    @GetMapping("/{id}")
    public VehicleDetailDto getDetailAVehicle(@PathVariable Integer id) {
        try {
            return vehicleService.getVehicleById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
