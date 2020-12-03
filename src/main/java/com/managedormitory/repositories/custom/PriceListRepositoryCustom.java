package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.price.PriceListDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceListRepositoryCustom {
    List<PriceListDto> getAllPriceList();

    int addPriceList(PriceListDto priceListDto);
}
