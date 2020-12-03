package com.managedormitory.models.dto.pagination;

import com.managedormitory.models.dto.price.PriceListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationPriceList {
    private Map<String, List<PriceListDto>> data;
    private int total;
}
