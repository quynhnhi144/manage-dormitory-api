package com.managedormitory.services;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.DetailRoomDto;
import com.managedormitory.models.dto.PaginationRoom;
import com.managedormitory.models.dto.RoomFilterDto;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    List<DetailRoomDto> getAllRoomDto();
    PaginationRoom paginationGetAllRooms( RoomFilterDto roomFilterDto, int skip, int take);
    DetailRoomDto getRoomById(Integer id);
}
