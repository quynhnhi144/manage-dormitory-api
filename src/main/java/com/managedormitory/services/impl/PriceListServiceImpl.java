package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.PriceList;
import com.managedormitory.models.dto.pagination.PaginationPriceList;
import com.managedormitory.models.dto.price.PriceListDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.repositories.PriceListRepository;
import com.managedormitory.repositories.custom.PriceListRepositoryCustom;
import com.managedormitory.services.PriceListService;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceListServiceImpl implements PriceListService {
    @Autowired
    private PriceListRepositoryCustom priceListRepositoryCustom;

    @Override
    public List<PriceListDto> getAllPriceList() {
        return priceListRepositoryCustom.getAllPriceList();
    }

    @Override
    public PaginationPriceList filterPriceList(int skip, int take, String searchText) {
        List<PriceListDto> priceLists = getAllPriceList();
        if (searchText != null && !searchText.equals("")) {
            String searchTextCurrent = searchText.toLowerCase() + StringUtil.DOT_STAR;

            priceLists = priceLists.stream()
                    .filter(priceList -> priceList.getName().toLowerCase().matches(searchTextCurrent))
                    .collect(Collectors.toList());
        }
        int total = priceLists.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<PriceListDto>> stringListMap = new HashMap<>();
        stringListMap.put("data", priceLists.subList(skip, lastElement));
        return new PaginationPriceList(stringListMap, total);
    }

    @Override
    public PriceListDto getPriceListById(Integer id) {
        List<PriceListDto> priceListDtos = getAllPriceList();
        return priceListDtos.stream()
                .filter(priceListDto -> priceListDto.getId() == id)
                .findFirst().orElseThrow();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PriceListDto addPriceList(PriceListDto priceListDto) {
        if (priceListRepositoryCustom.addPriceList(priceListDto) > 0) {
            return getAllPriceList().get(getAllPriceList().size() - 1);
        } else {
            throw new BadRequestException("Khong the tao moi tien thue");
        }
    }
}
