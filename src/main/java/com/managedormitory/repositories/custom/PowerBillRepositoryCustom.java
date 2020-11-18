package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.PowerBillImport;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PowerBillRepositoryCustom {
    List<PowerBillDto> getAllPowerBillByTime(LocalDate date);

    List<PowerBillDto> getAllPowerBillByMaxEndDate();

    int updatePowerBill(Integer roomId, PowerBillDetail powerBillDetail);

    int insertPowerBills(List<PowerBillDetail> powerBillDetails, List<PowerBillImport> powerBillImports);

    int insertPowerBill(Integer roomId, PowerBillDetail powerBillDetail);
}
