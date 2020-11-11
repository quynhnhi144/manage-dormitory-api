package com.managedormitory.repositories;

import com.managedormitory.models.dao.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Integer> {
}
