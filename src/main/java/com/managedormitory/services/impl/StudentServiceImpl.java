package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.*;
import com.managedormitory.models.dto.VehicleBillDto;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.repositories.StudentLeftRepository;
import com.managedormitory.repositories.StudentRepository;
import com.managedormitory.repositories.custom.RoomRepositoryCustom;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.CalculateMoney;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired
    private StudentLeftRepository studentLeftRepository;

    @Autowired
    private RoomRepositoryCustom roomRepositoryCustom;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<StudentDetailDto> getAllStudentDto() {
        List<Student> students = getAllStudents();
        List<StudentLeft> studentLefts = getAllStudentLeft();
        List<Integer> studentLeftIds = studentLefts.stream().mapToInt(StudentLeft::getId).boxed().collect(Collectors.toList());
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

            if (studentLeftIds.contains(student.getId())) {
                studentDetailDto.setActive(false);
            } else {
                studentDetailDto.setActive(true);
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
    public StudentDetailDto updateStudent(Integer id, StudentDto studentDto) throws BadRequestException {
        if (studentRepositoryCustom.updateStudent(id, studentDto) <= 0) {
            throw new BadRequestException("Cannot execute");
        }
        return getStudentById(id);
    }

    @Override
    public int countStudent() {
        return getAllStudentDto().size();
    }

    @Override
    public List<StudentLeft> getAllStudentLeft() {
        return studentLeftRepository.findAll();
    }

    @Override
    public RoomBillDto getDetailRoomRecently(Integer id) {
        return studentRepositoryCustom.getDetailRoomRecently(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentMoveDto getInfoMovingStudent(Integer id) {
        RoomBillDto roomBillDto = studentRepositoryCustom.getDetailRoomRecently(id);
        WaterBillDto waterBillDto = studentRepositoryCustom.getWaterBillRecently(id);
        VehicleBillDto vehicleBillDto = studentRepositoryCustom.getVehicleBillRecently(id);
        LocalDate currentDate = LocalDate.now();

        float remainingMoneyOfRoom = CalculateMoney.calculateRemainingMoney(currentDate, DateUtil.getLDateFromSDate(roomBillDto.getEndDate()), roomBillDto.getPrice() / roomBillDto.getMaxQuantity());
        float remainingMoneyOfWater = CalculateMoney.calculateRemainingMoney(currentDate, DateUtil.getLDateFromSDate(waterBillDto.getEndDate()), waterBillDto.getPrice());
        float remainingMoneyOfVehicle = CalculateMoney.calculateRemainingMoney(currentDate, DateUtil.getLDateFromSDate(vehicleBillDto.getEndDate()), vehicleBillDto.getPrice());

        return new StudentMoveDto(id, currentDate, remainingMoneyOfRoom, remainingMoneyOfWater, remainingMoneyOfVehicle, roomBillDto.getRoomId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addStudentLeft(StudentMoveDto studentMoveDto) {
        int resultStudentLeft = studentRepositoryCustom.addStudentLeft(studentMoveDto);
        int resultRoom = roomRepositoryCustom.updateQuantityStudent(studentMoveDto.getRoomId());
        if (resultStudentLeft > 0 && resultRoom > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot implement method!!!");
    }

}
