package com.managedormitory.repositories;

import com.managedormitory.models.dao.DetailRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRoomRepository extends JpaRepository<DetailRoom, Integer> {
}
