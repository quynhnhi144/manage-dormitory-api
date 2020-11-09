package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.*;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentUpdateDto;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.repositories.DetailRoomRepository;
import com.managedormitory.repositories.StudentRepository;
import com.managedormitory.repositories.VehicleRepository;
import com.managedormitory.repositories.WaterBillRepository;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentRepositoryCustom studentRepositoryCustom;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<StudentDetailDto> getAllStudentDto() {
        List<Student> students = getAllStudents();
        List<StudentDto> studentDtos = studentRepositoryCustom.getAllStudentByTime();
        List<StudentDetailDto> studentDetailDtosDetail = new ArrayList<>();
        List<Integer> studentDtosIdList = studentDtos.stream().mapToInt(StudentDto::getId).boxed().collect(Collectors.toList());
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            StudentDetailDto studentDetailDto = new StudentDetailDto();
            studentDetailDto.setId(student.getId());
            studentDetailDto.setName(student.getName());
            studentDetailDto.setBirthday(student.getBirthday());
            studentDetailDto.setPhone(student.getPhone());
            studentDetailDto.setEmail(student.getEmail());
            studentDetailDto.setAddress(student.getAddress());
            studentDetailDto.setStartingDateOfStay(DateUtil.getSDateFromLDate(student.getStartingDateOfStay()));
            studentDetailDto.setEndingDateOfStay(DateUtil.getSDateFromLDate(student.getEndingDateOfStay()));
            if (student.getRoom() == null) {
                studentDetailDto.setRoomDto(null);
            } else {
                studentDetailDto.setRoomDto(new RoomDto(student.getRoom()));
            }

            if (studentDtosIdList.contains(student.getId())) {
                studentDetailDto.setIsPayRoom(true);
                studentDetailDto.setIsPayWaterBill(true);
                studentDetailDto.setIsPayVehicleBill(true);
                studentDetailDto.setIsPayPowerBill(true);
            } else {
                studentDetailDto.setIsPayRoom(false);
                studentDetailDto.setIsPayWaterBill(false);
                studentDetailDto.setIsPayVehicleBill(false);
                studentDetailDto.setIsPayPowerBill(false);
            }
            studentDetailDtosDetail.add(studentDetailDto);
        }
        return studentDetailDtosDetail;
    }

    @Override
    public PaginationStudent paginationGetAllStudents(StudentFilterDto studentFilterDto, int skip, int take) {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        if (studentFilterDto.getCampusName() != null) {
            studentDetailDtos = studentDetailDtos.stream()
                    .filter(studentDto -> studentDto.getRoomDto() != null && studentDto.getRoomDto().getCampusName().toLowerCase().equals(studentFilterDto.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (studentFilterDto.getStudentNameOrRoomNameOrUserManager() != null && !studentFilterDto.getStudentNameOrRoomNameOrUserManager().equals("")) {
            String searchText = studentFilterDto.getStudentNameOrRoomNameOrUserManager().toLowerCase() + StringUtil.DOT_STAR;
            studentDetailDtos = studentDetailDtos.stream()
                    .filter(studentDto -> (studentDto.getRoomDto() != null && studentDto.getRoomDto().getName().toLowerCase().matches(searchText))
                            || (studentDto.getRoomDto() != null && studentDto.getRoomDto().getUserManager().toLowerCase().matches(searchText))
                            || studentDto.getName().toLowerCase().matches(searchText))
                    .collect(Collectors.toList());
        }

        int total = studentDetailDtos.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<StudentDetailDto>> studentDtoMap = new HashMap<>();
        studentDtoMap.put("data", studentDetailDtos.subList(skip, lastElement));
        return new PaginationStudent(studentDtoMap, total);
    }

    @Override
    public StudentDetailDto getStudentById(Integer id) {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        List<StudentDetailDto> studentDetailDtoById = studentDetailDtos.stream()
                .filter(studentDto -> studentDto.getId().equals(id))
                .collect(Collectors.toList());
        if (studentDetailDtoById.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return studentDetailDtoById.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentDetailDto updateStudent(Integer id, StudentUpdateDto studentUpdateDto) throws BadRequestException {
        if (studentRepositoryCustom.updateStudent(id, studentUpdateDto) <= 0) {
            throw new BadRequestException("Cannot execute");
        }
        return getStudentById(id);
    }

}
