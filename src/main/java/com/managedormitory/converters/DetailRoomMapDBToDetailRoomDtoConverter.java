package com.managedormitory.converters;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dto.DetailRoomDto;
import com.managedormitory.models.dto.DetailRoomMapDB;
import com.managedormitory.models.dto.StudentDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DetailRoomMapDBToDetailRoomDtoConverter {

    public DetailRoomDto convert(List<DetailRoomMapDB> sources) throws NotFoundException {
        if (sources.size() == 0) {
            throw new NotFoundException("Cannot find the id");
        } else {
            DetailRoomDto detailRoomDto = new DetailRoomDto();
            detailRoomDto.setRoomId(sources.get(0).getRoomId());
            detailRoomDto.setRoomName(sources.get(0).getRoomName());
            detailRoomDto.setQuantityStudent(sources.get(0).getQuantityStudent());
            detailRoomDto.setPriceRoom(sources.get(0).getPriceRoom());
            detailRoomDto.setCampusName(sources.get(0).getCampusName());
            detailRoomDto.setUserManager(sources.get(0).getUserManager());
            detailRoomDto.setTypeRoom(sources.get(0).getTypeRoom());
            if (sources.get(0).getStudentId() != null) {
                detailRoomDto.setPay(sources.get(0).getIsPay());
                List<StudentDto> students = new ArrayList<>();
                for (DetailRoomMapDB detailRoomMapDB : sources) {
                    StudentDto studentDto = StudentDto.builder()
                            .id(detailRoomMapDB.getStudentId())
                            .name(detailRoomMapDB.getStudentName())
                            .phone(detailRoomMapDB.getStudentPhoneNumber())
                            .email(detailRoomMapDB.getStudentEmail())
                            .build();
                    students.add(studentDto);
                }
                detailRoomDto.setStudents(students);
            }

            return detailRoomDto;
        }
    }
}
