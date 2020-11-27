package com.managedormitory.repositories;

import com.managedormitory.models.dao.VehicleLeft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleLeftRepository extends JpaRepository<VehicleLeft,Integer> {
}
