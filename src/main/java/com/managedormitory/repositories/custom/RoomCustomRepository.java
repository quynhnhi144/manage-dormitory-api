package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.RoomFilterDto;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;

@Repository
public interface RoomCustomRepository {
    Query filterRoom(RoomFilterDto roomFilterDto);
}
