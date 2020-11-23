package com.managedormitory.services.impl;

import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.*;
import com.managedormitory.models.dto.*;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.room.RoomPriceAndWaterPrice;
import com.managedormitory.models.dto.room.RoomPriceAndWaterPriceDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.repositories.StudentLeftRepository;
import com.managedormitory.repositories.StudentRepository;
import com.managedormitory.repositories.custom.*;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    private final StudentRepositoryCustom studentRepositoryCustom;

    private final StudentLeftRepository studentLeftRepository;

    private final RoomRepositoryCustom roomRepositoryCustom;

    private final StudentConvertToStudentDto studentConvertToStudentDto;

    private final WaterBillRepositoryCustom waterBillRepositoryCustom;

    private final DetailRoomRepositoryCustom detailRoomRepositoryCustom;

    private final SwitchRoomRepositoryCustom switchRoomRepositoryCustom;

    private final VehicleBillRepositoryCustom vehicleBillRepositoryCustom;

    public StudentServiceImpl(StudentRepository studentRepository, StudentRepositoryCustom studentRepositoryCustom, StudentLeftRepository studentLeftRepository, RoomRepositoryCustom roomRepositoryCustom, StudentConvertToStudentDto studentConvertToStudentDto, WaterBillRepositoryCustom waterBillRepositoryCustom, DetailRoomRepositoryCustom detailRoomRepositoryCustom, SwitchRoomRepositoryCustom switchRoomRepositoryCustom, VehicleBillRepositoryCustom vehicleBillRepositoryCustom) {
        this.studentRepository = studentRepository;
        this.studentRepositoryCustom = studentRepositoryCustom;
        this.studentLeftRepository = studentLeftRepository;
        this.roomRepositoryCustom = roomRepositoryCustom;
        this.studentConvertToStudentDto = studentConvertToStudentDto;
        this.waterBillRepositoryCustom = waterBillRepositoryCustom;
        this.detailRoomRepositoryCustom = detailRoomRepositoryCustom;
        this.switchRoomRepositoryCustom = switchRoomRepositoryCustom;
        this.vehicleBillRepositoryCustom = vehicleBillRepositoryCustom;
    }

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
        for (Student student : students) {
            StudentDetailDto studentDetailDto = new StudentDetailDto();
            studentDetailDto.setId(student.getId());
            studentDetailDto.setName(student.getName());
            studentDetailDto.setBirthday(student.getBirthday());
            studentDetailDto.setPhone(student.getPhone());
            studentDetailDto.setEmail(student.getEmail());
            studentDetailDto.setAddress(student.getAddress());
            studentDetailDto.setStartingDateOfStay(DateUtil.getSDateFromLDate(student.getStartingDateOfStay()));
            studentDetailDto.setEndingDateOfStay(DateUtil.getSDateFromLDate(student.getEndingDateOfStay()));
            studentDetailDto.setWaterPriceId(student.getPriceList().getId());
            if (student.getVehicle() == null) {
                studentDetailDto.setVehicleId(null);
            } else {
                studentDetailDto.setVehicleId(student.getVehicle().getId());
            }
            if (student.getRoom() == null) {
                studentDetailDto.setRoomDto(null);
            } else {
                studentDetailDto.setRoomDto(new RoomDto(student.getRoom()));
            }

            if (studentDtosIdList.contains(student.getId())) {
                studentDetailDto.setIsPayRoom(true);
                studentDetailDto.setIsPayWaterBill(true);
            } else {
                studentDetailDto.setIsPayRoom(false);
                studentDetailDto.setIsPayWaterBill(false);
            }

            if (studentLeftIds.contains(student.getId())) {
                studentDetailDto.setActive(false);
            }
            else {
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

    @Override
    public RoomPriceAndWaterPriceDto getRoomPriceAndWaterPrice(Integer roomId) {
        LocalDate currentDate = LocalDate.now();
        RoomPriceAndWaterPrice roomPriceAndWaterPrice = roomRepositoryCustom.getRoomPriceAndWaterPrice(roomId);

        LocalDate currentDateRoom = currentDate;
        LocalDate endDateRoom;

        LocalDate currentDateWater = currentDate;
        LocalDate endDateWater;

        LocalDate currentDateVehicle = currentDate;
        LocalDate endDateVehicle = LocalDate.now();
        float remainingMoneyOfRoom;
        float remainingMoneyOfWater;
        float remainingMoneyOfVehicle;
        System.out.println("maxdateRoom: " + DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateRoomBill()));
        System.out.println("waterRoom: " + DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateWaterBill()));
        if (roomPriceAndWaterPrice == null) {
            endDateRoom = currentDate.plus(30, ChronoUnit.DAYS);
            endDateWater = currentDate.plus(30, ChronoUnit.DAYS);
        } else {
            if (DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateRoomBill()).isBefore(currentDate)) {
                endDateRoom = DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateRoomBill()).plus(30, ChronoUnit.DAYS);
                endDateWater = DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateWaterBill()).plus(30, ChronoUnit.DAYS);
            } else {
                currentDateRoom = DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateRoomBill());
                currentDateWater = DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateWaterBill());
                endDateRoom = currentDate;
                endDateWater = currentDate;
            }

            if (roomPriceAndWaterPrice.getVehicleId() == null) {
                currentDateVehicle = null;
                endDateVehicle = null;
            } else {
                if (DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateVehicleBill()).isBefore(currentDate)) {
                    endDateVehicle = DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateVehicleBill()).plus(30, ChronoUnit.DAYS);
                } else {
                    currentDateVehicle = DateUtil.getLDateFromSDate(roomPriceAndWaterPrice.getMaxDateVehicleBill());
                    endDateVehicle = currentDate;
                }
            }
        }
        remainingMoneyOfRoom = CalculateMoney.calculateRemainingMoney(endDateRoom, currentDateRoom, roomPriceAndWaterPrice.getRoomPrice() / roomPriceAndWaterPrice.getMaxQuantityStudent());
        remainingMoneyOfWater = CalculateMoney.calculateRemainingMoney(endDateWater, currentDateWater, roomPriceAndWaterPrice.getWaterPrice());
        remainingMoneyOfVehicle = CalculateMoney.calculateRemainingMoney(endDateVehicle, currentDateVehicle, roomPriceAndWaterPrice.getVehiclePrice());

        return new RoomPriceAndWaterPriceDto(roomId, roomPriceAndWaterPrice.getRoomName(), endDateRoom, currentDateRoom, remainingMoneyOfRoom, roomPriceAndWaterPrice.getWaterPriceId(), endDateWater, currentDateWater, remainingMoneyOfWater, roomPriceAndWaterPrice.getVehicleId(), endDateVehicle, currentDateVehicle, remainingMoneyOfVehicle, roomPriceAndWaterPrice.getMaxQuantityStudent());
    }

    @Override
    public DurationBetweenTwoRoom durationMoneyBetweenTwoRoom(Integer oldRoomId, Integer newRoomId) {
        LocalDate currentDate = LocalDate.now();
        if (oldRoomId != null && newRoomId != null) {
            RoomPriceAndWaterPriceDto oldRoom = getRoomPriceAndWaterPrice(oldRoomId);
            System.out.println("tien phong cu: " + oldRoom.getMoneyOfRoomMustPay());
            System.out.println("tien nuoc cu: " + oldRoom.getMoneyOfWaterMustPay());
            System.out.println("tien xe cu: " + oldRoom.getMoneyOfVehicleMustPay());
            System.out.println("oldRoom: " + oldRoom);
            RoomPriceAndWaterPriceDto newRoom = getRoomPriceAndWaterPrice(newRoomId);
            System.out.println("========================");
            System.out.println("tien phong moi: " + newRoom.getMoneyOfRoomMustPay());
            System.out.println("tien nuoc moi: " + newRoom.getMoneyOfWaterMustPay());
            System.out.println("tien xe moi: " + newRoom.getMoneyOfVehicleMustPay());
            System.out.println("oldRoom: " + newRoom);


            float durationRoomMoney = newRoom.getMoneyOfRoomMustPay() - oldRoom.getMoneyOfRoomMustPay();
            float durationWaterMoney = newRoom.getMoneyOfWaterMustPay() - oldRoom.getMoneyOfWaterMustPay();
            float durationVehicleMoney = newRoom.getMoneyOfVehicleMustPay() - oldRoom.getMoneyOfVehicleMustPay();

            return new DurationBetweenTwoRoom(oldRoomId, newRoomId, oldRoom.getRoomName(), newRoom.getRoomName(), currentDate, newRoom.getRoomEndDate(), durationRoomMoney, currentDate, newRoom.getWaterEndDate(), durationWaterMoney, currentDate, newRoom.getVehicleEndDate(), durationVehicleMoney);
        }
        throw new BadRequestException("Không được để trống id của phòng mới!!!!");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentDto addStudent(StudentNewDto studentNewDto) {
        int resultAddStudent = studentRepositoryCustom.addStudent(studentNewDto.getStudentDto());

        if (resultAddStudent > 0) {
            StudentDto studentDto = studentConvertToStudentDto.convert(getAllStudents().get(getAllStudents().size() - 1));
            RoomBillDto roomBillDto = studentNewDto.getRoomBillDto();
            roomBillDto.setStudentId(studentDto.getId());
            roomBillDto.setStudentName(studentDto.getName());
            roomBillDto.setRoomId(studentDto.getRoomId());

            WaterBillDto waterBillDto = studentNewDto.getWaterBillDto();
            waterBillDto.setStudentId(studentDto.getId());
            waterBillDto.setStudentName(studentDto.getName());
            waterBillDto.setRoomId(studentDto.getRoomId());
            int resultUpdateQuantityStudent = roomRepositoryCustom.updateQuantityStudent(studentDto.getRoomId());
            int resultDetailRoom = detailRoomRepositoryCustom.addDetailRoom(roomBillDto);
            int resultWaterBill = waterBillRepositoryCustom.addWaterBill(waterBillDto);

            if (resultUpdateQuantityStudent > 0 && resultDetailRoom > 0 && resultWaterBill > 0) {
                return studentDto;
            } else {
                throw new BadRequestException("Không thể thực hiện hành động này!!!");
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int switchRoomForStudent(InfoSwitchRoom infoSwitchRoom, Integer studentId) {
        LocalDate currentDate = LocalDate.now();
        System.out.println("Info switch room: " + infoSwitchRoom);
        Integer oldRoomId = infoSwitchRoom.getOldRoomId();
        Integer newRoomId = infoSwitchRoom.getNewRoomId();
        int resultUpdateRoomIdForStudent = studentRepositoryCustom.updateRoomIdForStudent(studentId, newRoomId);
        int resultUpdateQuantityStudentOldRoom = roomRepositoryCustom.updateQuantityStudent(oldRoomId);
        int resultUpdateQuantityStudentNewRoom = roomRepositoryCustom.updateQuantityStudent(newRoomId);

        int resultAddDetailRoom = 0;
        int resultAddWaterBill = 0;
        int resultAddVehicleBill = 0;
        DurationBetweenTwoRoom durationBetweenTwoRoom = durationMoneyBetweenTwoRoom(oldRoomId, newRoomId);
        SwitchRoomHistoryDto switchRoomHistoryDto;
        if (durationBetweenTwoRoom.getDurationRoomMoney() < 0 && durationBetweenTwoRoom.getDurationWaterMoney() < 0 && durationBetweenTwoRoom.getDurationVehicleMoney() < 0) {
            switchRoomHistoryDto = new SwitchRoomHistoryDto(null, durationBetweenTwoRoom.getOldRoomName(), durationBetweenTwoRoom.getNewRoomName(), Math.abs(durationBetweenTwoRoom.getDurationRoomMoney()), 0, Math.abs(durationBetweenTwoRoom.getDurationWaterMoney()), 0, Math.abs(durationBetweenTwoRoom.getDurationVehicleMoney()), 0, studentId, DateUtil.getSDateFromLDate(currentDate));
            resultAddDetailRoom = detailRoomRepositoryCustom.addDetailRoom(infoSwitchRoom.getRoomBill());
            resultAddWaterBill = waterBillRepositoryCustom.addWaterBill(infoSwitchRoom.getWaterBill());
            resultAddVehicleBill = vehicleBillRepositoryCustom.addVehicleBillRepository(infoSwitchRoom.getVehicleBill());
        } else {
            switchRoomHistoryDto = new SwitchRoomHistoryDto(null, durationBetweenTwoRoom.getOldRoomName(), durationBetweenTwoRoom.getNewRoomName(), 0, durationBetweenTwoRoom.getDurationRoomMoney(), 0, durationBetweenTwoRoom.getDurationWaterMoney(), 0, durationBetweenTwoRoom.getDurationVehicleMoney(), studentId, DateUtil.getSDateFromLDate(currentDate));
        }
        int resultAddSwitchRoomHistory = switchRoomRepositoryCustom.addSwitchRoomHistory(switchRoomHistoryDto);
        if (resultUpdateRoomIdForStudent > 0 && resultUpdateQuantityStudentNewRoom > 0
                && resultUpdateQuantityStudentOldRoom > 0 && resultAddDetailRoom >= 0 && resultAddWaterBill >= 0 && resultAddVehicleBill >= 0) {
            return 1;
        }
        return 0;
    }

}
