package com.managedormitory.services;

import com.managedormitory.models.dto.pagination.PaginationRegisterRoom;
import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.registerRoom.RegisterRoomIncludePayment;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.RegisterRoomFilter;

import java.util.List;

public interface RegisterRoomService {
    List<RegisterRoomDto> getAllRegisterRoom();

    PaginationRegisterRoom paginationGetAllRegisterRoom(RegisterRoomFilter registerRoomFilter, int skip, int take);

    RegisterRoomIncludePayment getRegisterRoom(Integer id);

    int deleteRegisterRoom(Integer id);

    int addStudentFromRegisterRoom(Integer registerRoomId, StudentNewDto studentNewDto);
}
