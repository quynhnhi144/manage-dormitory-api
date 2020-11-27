package com.managedormitory.services;

import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dao.StudentLeft;
import com.managedormitory.models.dto.DurationBetweenTwoRoom;
import com.managedormitory.models.dto.InfoSwitchRoom;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.room.RoomPriceAndWaterPrice;
import com.managedormitory.models.dto.room.RoomPriceAndWaterPriceDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.StudentFilterDto;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();

    List<StudentDetailDto> getAllStudentDto();

    PaginationStudent paginationGetAllStudents(StudentFilterDto studentFilterDto, int skip, int take);

    StudentDetailDto getStudentById(Integer id);

    StudentDetailDto updateStudent(Integer id, StudentDto studentDto);

    int countStudent();

    List<StudentLeft> getAllStudentLeft();

    RoomBillDto getDetailRoomRecently(Integer id);

    StudentMoveDto getInfoMovingStudent(Integer id);

    int addStudentLeft(StudentMoveDto studentMoveDto);

    RoomPriceAndWaterPriceDto getRoomPriceAndWaterPrice(Integer roomId);

    DurationBetweenTwoRoom durationMoneyBetweenTwoRoom(Integer oldRoomId, Integer newRoomId);

    StudentDto addStudent(StudentNewDto studentNewDto);

    int switchRoomForStudent(InfoSwitchRoom infoSwitchRoom, Integer studentId);

    List<StudentDetailDto> getStudentsByIdCard(String idCard);
}
