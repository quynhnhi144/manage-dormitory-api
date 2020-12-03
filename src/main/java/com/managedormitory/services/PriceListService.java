package com.managedormitory.services;

import com.managedormitory.models.dto.pagination.PaginationPriceList;
import com.managedormitory.models.dto.price.PriceListDto;

import java.util.List;

public interface PriceListService {
    List<PriceListDto> getAllPriceList();

    PaginationPriceList filterPriceList(int skip, int take, String searchText);

    PriceListDto getPriceListById(Integer id);

    PriceListDto addPriceList(PriceListDto priceListDto);
}
