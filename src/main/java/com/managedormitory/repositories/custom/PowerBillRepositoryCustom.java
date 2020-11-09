package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.powerbill.PowerBillDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerBillRepositoryCustom {
    List<PowerBillDto> getAllPowerBillByTime();
}
