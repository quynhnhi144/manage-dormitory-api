package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.VehicleBillDto;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepositoryCustom {
    List<StudentDto> getAllStudentByTime();

    int updateStudent(Integer id, StudentDto studentDto);

    int updateRoomIdForStudent(Integer studentId, Integer newRoomId);

    Optional<RoomBillDto> getDetailRoomRecently(Integer id);

    Optional<WaterBillDto> getWaterBillRecently(Integer id);

    Optional<VehicleBillDto> getVehicleBillRecently(Integer id);

    int addStudentLeft(StudentMoveDto studentMoveDto);

    int addStudent(StudentDto studentDto);
}
