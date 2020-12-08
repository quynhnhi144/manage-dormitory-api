package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterRoomRepositoryCustom {
    List<RegisterRoomDto> getAllRegisterRoom();

    int deleteRegisterRoom(Integer id);

    int countRegisterOfARoom(Integer roomId);
}
