package com.managedormitory.services;

import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dao.StudentLeft;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.pagination.PaginationRoom;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.room.RoomPriceAndWaterPrice;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import com.managedormitory.models.filter.RoomFilterDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();

    List<DetailRoomDto> getAllDetailRoomDto();

    PaginationRoom paginationGetAllRooms(RoomFilterDto roomFilterDto, int skip, int take);

    DetailRoomDto getRoomById(Integer id);

    ByteArrayInputStream exportExcelRooms() throws IOException;

    DetailRoomDto updateTypeRoom(Integer id, DetailRoomDto room);

    List<DetailRoomDto> getAllRemainingRoomDto(String searchText);

    List<StudentMoveDto> getPaymentOfAllStudentsInRoom(Integer id);

    int countRoom();

    int countRemainingRoom();
}
