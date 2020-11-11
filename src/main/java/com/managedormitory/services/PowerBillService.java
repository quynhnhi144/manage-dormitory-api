package com.managedormitory.services;

import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.models.filter.PowerBillFilter;

import java.util.List;

public interface PowerBillService {
    List<PowerBillDetail> getAllDetailPowerBills();
    PaginationPowerBill paginationGetAllPowerBills(PowerBillFilter powerBillFilter, int skip, int take);
    float calculatePowerBill(PowerBillDto powerBillDto);
    PowerBillDetail getAPowerBill(Integer roomId);
}
