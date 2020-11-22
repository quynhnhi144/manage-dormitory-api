package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.repositories.custom.WaterBillRepositoryCustom;
import com.managedormitory.services.WaterBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaterBillServiceImpl implements WaterBillService {
    @Autowired
    private WaterBillRepositoryCustom waterBillRepositoryCustom;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addWaterBill(WaterBillDto waterBillDto) {
        if (waterBillRepositoryCustom.addWaterBill(waterBillDto) > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot implement method!!!");
    }
}
