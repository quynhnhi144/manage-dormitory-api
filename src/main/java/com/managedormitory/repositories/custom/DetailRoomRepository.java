package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.DetailRoomMapDB;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRoomRepository {
    List<DetailRoomMapDB> getDetailARoom(Integer id);
}
