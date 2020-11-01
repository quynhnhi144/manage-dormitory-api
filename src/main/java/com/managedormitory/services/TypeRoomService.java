package com.managedormitory.services;

import com.managedormitory.models.dao.TypeRoom;

import java.util.Optional;

public interface TypeRoomService {
    Optional<TypeRoom> getTypeRoomById(Integer id);
}
