package com.managedormitory.repositories;

import com.managedormitory.models.dao.WaterBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterBillRepository extends JpaRepository<WaterBill, Integer> {
}
