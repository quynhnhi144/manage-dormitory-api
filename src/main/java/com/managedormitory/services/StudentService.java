package com.managedormitory.services;

import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dao.StudentLeft;
import com.managedormitory.models.dto.DurationBetweenTwoRoom;
import com.managedormitory.models.dto.InfoSwitchRoom;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.room.InfoMoneyDto;
import com.managedormitory.models.dto.student.*;
import com.managedormitory.models.filter.StudentFilterDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();

    List<StudentDetailDto> getAllStudentDto();

    List<StudentDetailDto> getAllStudentDtoActive();

    PaginationStudent paginationGetAllStudents(StudentFilterDto studentFilterDto, int skip, int take);

    StudentDetailDto getStudentById(Integer id);

    StudentDetailDto updateStudent(Integer id, StudentDto studentDto);

    int countStudent();

    List<StudentLeft> getAllStudentLeft();

    RoomBillDto getDetailRoomRecently(Integer id);

    StudentLeftDto getInfoMovingStudent(Integer id);

    int addStudentLeft(StudentLeftDto studentMoveDto);

    InfoMoneyDto getInfoMoneyDtoForNewStudent(Integer roomId);

    StudentBill getInfoMoneyRoomPaymentDto(Integer studentId);

    DurationBetweenTwoRoom durationMoneyBetweenTwoRoom(Integer oldRoomId, Integer newRoomId);

    StudentDto addStudent(StudentNewDto studentNewDto);

    ByteArrayInputStream exportPDFStudentNew(StudentNewDto studentNewDto);

    ByteArrayInputStream exportPDFStudentRemove(StudentLeftDto studentMoveDto);

    ByteArrayInputStream exportPDFStudentSwitchRoom(InfoSwitchRoom studentSwitchRoom);

    ByteArrayInputStream exportPDFStudentPayment(StudentBill studentBill);

    ByteArrayInputStream exportExcel() throws IOException;

    int switchRoomForStudent(InfoSwitchRoom infoSwitchRoom, Integer studentId);

    List<StudentDetailDto> getStudentsByIdCard(String idCard);

    int registerRemainingRoomForStudent(RegisterRoomDto registerRoomDto);

    int addPaymentForStudent(StudentBill studentBill);
}
