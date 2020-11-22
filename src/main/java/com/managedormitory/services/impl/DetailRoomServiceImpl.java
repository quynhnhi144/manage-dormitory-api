package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.repositories.custom.DetailRoomRepositoryCustom;
import com.managedormitory.services.DetailRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetailRoomServiceImpl implements DetailRoomService {
    @Autowired
    private DetailRoomRepositoryCustom detailRoomRepositoryCustom;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDetailRoom(RoomBillDto roomBillDto) {
        if (detailRoomRepositoryCustom.addDetailRoom(roomBillDto) > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot implement method!!!");
    }
}
