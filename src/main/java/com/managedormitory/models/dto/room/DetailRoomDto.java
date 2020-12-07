package com.managedormitory.models.dto.room;

import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dao.TypeRoom;
import com.managedormitory.models.dto.student.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailRoomDto {
    private Integer id;
    private String name;
    private Integer quantityStudent;
    private Float priceRoom;
    private Float priceWater;
    private Float priceVehicle;
    private String campusName;
    private TypeRoom typeRoom;
    private String userManager;
    private List<StudentDto> students;
    private Boolean isPayRoom;
    private Boolean isPayWaterBill;

    public DetailRoomDto(Room room, StudentConvertToStudentDto studentConvertToStudentDto) {
        this.id = room.getId();
        this.name = room.getName();
        this.campusName = room.getCampus().getName();
        this.quantityStudent = room.getQuantityStudent();
        this.students = studentConvertToStudentDto.convert(room.getStudents());
    }
}
