package com.managedormitory.services.impl;

import com.managedormitory.models.dao.TypeRoom;
import com.managedormitory.repositories.TypeRoomRepository;
import com.managedormitory.services.TypeRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TypeRoomServiceImpl implements TypeRoomService {
    @Autowired
    private TypeRoomRepository typeRoomRepository;

    @Override
    public Optional<TypeRoom> getTypeRoomById(Integer id) {
        return typeRoomRepository.findById(id);
    }
}
