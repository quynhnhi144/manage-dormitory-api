package com.managedormitory.services;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.pagination.PaginationRoom;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.student.StudentInRoomDto;
import com.managedormitory.models.filter.RoomFilterDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    List<DetailRoomDto> getAllDetailRoomDto();
    List<RoomDto> getAllRoomDto();
    PaginationRoom paginationGetAllRooms(RoomFilterDto roomFilterDto, int skip, int take);
    DetailRoomDto getRoomById(Integer id);
    ByteArrayInputStream exportExcelRooms() throws IOException;
    boolean deleteStudentInRoom(Integer roomId, StudentInRoomDto studentInRoomDtoNew);
    DetailRoomDto updateTypeRoom(Integer id,DetailRoomDto room);
}
