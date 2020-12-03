package com.managedormitory.controllers;

import com.managedormitory.models.dto.pagination.PaginationPriceList;
import com.managedormitory.models.dto.price.PriceListDto;
import com.managedormitory.services.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/price-list")
public class PriceListController {
    @Autowired
    private PriceListService priceListService;

    @GetMapping
    public PaginationPriceList filterPriceList(@RequestParam(required = false) String searchText, @RequestParam int skip, @RequestParam int take) {
        return priceListService.filterPriceList(skip, take, searchText);
    }

    @GetMapping("/{id}")
    public PriceListDto getPriceListById(@PathVariable Integer id) {
        return priceListService.getPriceListById(id);
    }

    @PostMapping
    public PriceListDto addPriceList(@RequestBody PriceListDto priceListDto) {
        return priceListService.addPriceList(priceListDto);
    }
}
