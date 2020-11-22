package com.managedormitory.controllers;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.services.WaterBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/waterBills")
public class WaterBillController {
    @Autowired
    private WaterBillService waterBillService;

    @PostMapping("/new-water-bill")
    public int addWaterBill(@RequestBody WaterBillDto waterBillDto) {
        if (waterBillService.addWaterBill(waterBillDto) > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot impelement method!!!");
    }
}
