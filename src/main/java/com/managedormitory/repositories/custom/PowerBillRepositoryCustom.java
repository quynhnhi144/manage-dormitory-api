package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerBillRepositoryCustom {
    List<PowerBillDto> getAllPowerBillByTime();
    int updatePowerBill(Integer roomId, PowerBillDetail powerBillDetail);
}
