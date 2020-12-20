package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentLeftDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepositoryCustom {
    List<StudentDto> getAllStudentByTime();

    int updateStudent(Integer id, StudentDto studentDto);

    int updateRoomIdForStudent(Integer studentId, Integer newRoomId);

    Optional<WaterBillDto> getWaterBillRecently(Integer id);

    int addStudentLeft(StudentLeftDto studentMoveDto);

    int addStudent(StudentDto studentDto);

//    int registerRemainingRoomForStudent(RegisterRoomDto registerRoomDto);
}
