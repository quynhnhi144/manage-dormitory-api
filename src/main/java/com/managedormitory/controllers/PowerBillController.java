package com.managedormitory.controllers;

import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.filter.PowerBillFilter;
import com.managedormitory.services.PowerBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/powerBills")
public class PowerBillController {

    @Autowired
    private PowerBillService powerBillService;

    @GetMapping
    public PaginationPowerBill filterPowerBill(@RequestParam(required = false) String campusName, @RequestParam(required = false) String roomName, @RequestParam int skip, @RequestParam int take) {
        PowerBillFilter powerBillFilter = PowerBillFilter.builder().campusName(campusName).roomName(roomName).build();
        return powerBillService.paginationGetAllPowerBills(powerBillFilter, skip, take);
    }

    @GetMapping("/{roomId}")
    public PowerBillDetail getADetailPowerBill(@PathVariable Integer roomId) {
        return powerBillService.getAPowerBill(roomId);
    }
}
