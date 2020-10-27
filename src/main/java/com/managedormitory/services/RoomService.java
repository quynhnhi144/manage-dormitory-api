package com.managedormitory.services;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.DetailRoomDto;
import com.managedormitory.models.dto.PaginationRoom;
import com.managedormitory.models.dto.RoomDto;
import com.managedormitory.models.filter.RoomFilterDto;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    List<DetailRoomDto> getAllDetailRoomDto();
    List<RoomDto> getAllRoomDto();
    PaginationRoom paginationGetAllRooms( RoomFilterDto roomFilterDto, int skip, int take);
    DetailRoomDto getRoomById(Integer id);
    ByteArrayInputStream exportExcelRooms() throws IOException;
}
