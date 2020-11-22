package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.WaterBillDto;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterBillRepositoryCustom {
    int addWaterBill(WaterBillDto waterBillDto);
}
