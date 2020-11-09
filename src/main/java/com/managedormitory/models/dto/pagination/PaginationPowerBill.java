package com.managedormitory.models.dto.pagination;

import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationPowerBill {

    private Map<String, List<PowerBillDetail>> data;
    private int total;
}
