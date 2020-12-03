package com.managedormitory.controllers;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dto.vehicle.*;
import com.managedormitory.models.dto.pagination.PaginationVehicle;
import com.managedormitory.models.filter.VehicleFilter;
import com.managedormitory.services.VehicleService;
import com.managedormitory.utils.StringUtil;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/{id}/vehicleLeft")
    public VehicleMoveDto getVehicleBill(@PathVariable Integer id) {
        try {
            return vehicleService.getInfoMovingVehicle(id);
        } catch (Exception e) {
            throw new NotFoundException("Cannot find this " + id);
        }
    }

    @GetMapping("/{studentId}/paymentVehicle")
    public VehicleBillDto getPaymentVehicle(@PathVariable Integer studentId) {
        return vehicleService.VehicleBillInfoForNewVehicle(studentId);
    }

    @GetMapping("/exportExcel")
    public ResponseEntity<Resource> exportExcelFile() {
        try {
            InputStreamResource file = new InputStreamResource(vehicleService.exportExcel());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + StringUtil.FILE_NAME_EXCEL_VEHICLE)
                    .contentType(MediaType.parseMediaType(StringUtil.MEDIA_TYPE))
                    .body(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("/vehicle")
    public int addVehicle(@RequestBody VehicleNew vehicleNew) {
        return vehicleService.addVehicle(vehicleNew);
    }

    @PostMapping("/vehicleLeft")
    public int addVehicleLeft(@RequestBody VehicleMoveDto vehicleMoveDto) {
        return vehicleService.addVehicleLeft(vehicleMoveDto);
    }

    @PutMapping("/{id}")
    public VehicleDetailDto updateVehicle(@PathVariable Integer id, @RequestBody VehicleDetailDto vehicleDetailDto) {
        return vehicleService.updateVehicle(id, vehicleDetailDto);
    }
}
