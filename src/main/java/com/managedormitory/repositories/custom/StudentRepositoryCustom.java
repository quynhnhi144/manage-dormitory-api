package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.VehicleBillDto;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepositoryCustom {
    List<StudentDto> getAllStudentByTime();

    int updateStudent(Integer id, StudentDto studentDto);

    int updateRoomIdForStudent(Integer studentId, Integer newRoomId);

    RoomBillDto getDetailRoomRecently(Integer id);

    WaterBillDto getWaterBillRecently(Integer id);

    VehicleBillDto getVehicleBillRecently(Integer id);

    int addStudentLeft(StudentMoveDto studentMoveDto);

    int addStudent(StudentDto studentDto);
}
