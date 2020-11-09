package com.managedormitory.repositories;

import com.managedormitory.models.dao.PowerBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerBillRepository extends JpaRepository<PowerBill, Integer> {
}
