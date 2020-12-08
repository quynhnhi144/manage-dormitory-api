package com.managedormitory.services.impl;

import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.helper.RoomExcelHelper;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dao.StudentLeft;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.pagination.PaginationRoom;
import com.managedormitory.models.dto.room.RoomAndCountRegister;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.room.RoomPayment;
import com.managedormitory.models.dto.student.StudentBill;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentLeftDto;
import com.managedormitory.models.filter.RoomFilterDto;
import com.managedormitory.repositories.PriceListRepository;
import com.managedormitory.repositories.RoomRepository;
import com.managedormitory.repositories.StudentLeftRepository;
import com.managedormitory.repositories.custom.RegisterRoomRepositoryCustom;
import com.managedormitory.repositories.custom.RoomRepositoryCustom;
import com.managedormitory.services.RoomService;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomRepositoryCustom roomRepositoryCustom;

    @Autowired
    private StudentConvertToStudentDto studentConvertToStudentDto;

    @Autowired
    private StudentLeftRepository studentLeftRepository;

    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RegisterRoomRepositoryCustom registerRoomRepositoryCustom;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<DetailRoomDto> getAllDetailRoomDto() {
        List<Room> rooms = getAllRooms();
        List<RoomDto> roomDtos = roomRepositoryCustom.getAllRoomByTime();
        List<DetailRoomDto> detailRoomDtos = new ArrayList<>();
        List<Integer> roomDtosDdList = roomDtos.stream().mapToInt(RoomDto::getId).boxed().collect(Collectors.toList());
        List<Integer> studentLeftIds = studentLeftRepository.findAll().stream().map(StudentLeft::getId).collect(Collectors.toList());
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            DetailRoomDto detailRoomDto = new DetailRoomDto();
            detailRoomDto.setId(room.getId());
            detailRoomDto.setName(room.getName());
            List<StudentDto> studentDtos = studentConvertToStudentDto.convert(room.getStudents().stream()
                    .filter(student -> !studentLeftIds.contains(student.getId()))
                    .collect(Collectors.toList())
            );
            studentDtos.sort(Comparator.comparing(StudentDto::getId));
            detailRoomDto.setStudents(studentDtos);
            detailRoomDto.setQuantityStudent(room.getQuantityStudent());
            detailRoomDto.setPriceWater(priceListRepository.findById(2).orElse(null).getPrice());
            detailRoomDto.setPriceVehicle(priceListRepository.findById(3).orElse(null).getPrice());
            if (room.getTypeRoom() == null) {
                detailRoomDto.setTypeRoom(null);
            } else {
                detailRoomDto.setTypeRoom(room.getTypeRoom());
            }
            detailRoomDto.setCampusName(room.getCampus().getName());
            if (room.getCampus().getUserManager() == null) {
                detailRoomDto.setUserManager(null);
            } else {
                detailRoomDto.setUserManager(room.getCampus().getUserManager().getFullName());
            }
            detailRoomDto.setPriceRoom(room.getPriceList().getPrice());
            if (roomDtosDdList.contains(room.getId())) {
                detailRoomDto.setIsPayRoom(true);
                detailRoomDto.setIsPayWaterBill(true);
            } else {
                detailRoomDto.setIsPayRoom(false);
                detailRoomDto.setIsPayWaterBill(false);
            }
            detailRoomDtos.add(detailRoomDto);
        }
        return detailRoomDtos;
    }

    @Override
    public PaginationRoom paginationGetAllRooms(RoomFilterDto roomFilterDto, int skip, int take) {
        List<DetailRoomDto> detailRoomDtos = getAllDetailRoomDto();
        if (roomFilterDto.getCampusName() != null) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getCampusName().toLowerCase().equals(roomFilterDto.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (roomFilterDto.getRoomNameOrUserManager() != null && !roomFilterDto.getRoomNameOrUserManager().equals("")) {
            String searchText = roomFilterDto.getRoomNameOrUserManager().toLowerCase() + StringUtil.DOT_STAR;
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getUserManager() != null && detailRoomDto.getUserManager().toLowerCase().matches(searchText)
                            || detailRoomDto.getName().toLowerCase().matches(searchText))
                    .collect(Collectors.toList());
        }
        if (roomFilterDto.getQuantityStudent() != null && roomFilterDto.getQuantityStudent() >= 0) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getQuantityStudent().equals(roomFilterDto.getQuantityStudent()))
                    .collect(Collectors.toList());
        }
        if (roomFilterDto.getTypeRoomId() != null && roomFilterDto.getTypeRoomId() != 0) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getTypeRoom() != null && detailRoomDto.getTypeRoom().getId() == roomFilterDto.getTypeRoomId())
                    .collect(Collectors.toList());
        }
        int total = detailRoomDtos.size();
        int lastElement;
        if (take < total) {
            if (skip + take < total) {
                lastElement = skip + take;
            } else {
                lastElement = total;
            }
        } else {
            lastElement = total;
        }

        Map<String, List<DetailRoomDto>> roomDtoMap = new HashMap<>();
        roomDtoMap.put("data", detailRoomDtos.subList(skip, lastElement));
        return new PaginationRoom(roomDtoMap, total);
    }

    @Override
    public DetailRoomDto getRoomById(Integer id) throws NotFoundException {
        List<DetailRoomDto> detailRoomDtos = getAllDetailRoomDto();
        return detailRoomDtos.stream()
                .filter(detailRoomDto -> detailRoomDto.getId() == id)
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + " " + b);
                }).get();
    }

    @Override
    public ByteArrayInputStream exportExcelRooms() throws IOException {
        List<DetailRoomDto> detailRoomDtos = getAllDetailRoomDto();
        RoomExcelHelper roomExcelHelper = new RoomExcelHelper(detailRoomDtos);
        try {
            ByteArrayInputStream inputStream = roomExcelHelper.export();
            return inputStream;
        } catch (IOException e) {
            throw new IOException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DetailRoomDto updateTypeRoom(Integer id, DetailRoomDto room) throws BadRequestException {
        if (roomRepositoryCustom.updateTypeRoom(id, room) <= 0) {
            throw new BadRequestException("Cannot implement update");
        }
        return getRoomById(id);
    }

    @Override
    public List<RoomAndCountRegister> getAllRemainingRoomDto(String searchText) {
        List<DetailRoomDto> detailRoomDtos = getAllDetailRoomDto().stream()
                .filter(detailRoomDto -> detailRoomDto.getTypeRoom() != null && detailRoomDto.getTypeRoom().getMaxQuantity() - detailRoomDto.getQuantityStudent() > 0
                        || (detailRoomDto.getQuantityStudent() == 0 && detailRoomDto.getStudents().size() == 0))
                .collect(Collectors.toList());


        if (searchText != null && !searchText.equals("")) {
            String searchTextConverted = searchText.toLowerCase() + StringUtil.DOT_STAR;
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getName().toLowerCase().matches(searchTextConverted)
                            || detailRoomDto.getTypeRoom().getName().toLowerCase().matches(searchTextConverted))
                    .collect(Collectors.toList());
        }
        return detailRoomDtos.stream()
                .map(detailRoomDto -> new RoomAndCountRegister(detailRoomDto, registerRoomRepositoryCustom.countRegisterOfARoom(detailRoomDto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailRoomDto> getEnoughConditionSwitchRooms() {
        return getAllDetailRoomDto().stream()
                .filter(detailRoomDto -> detailRoomDto.getTypeRoom() != null && detailRoomDto.getTypeRoom().getMaxQuantity() - detailRoomDto.getQuantityStudent() > 0 && detailRoomDto.getIsPayRoom() && detailRoomDto.getIsPayWaterBill()
                        || (detailRoomDto.getQuantityStudent() == 0 && detailRoomDto.getStudents().size() == 0))
                .collect(Collectors.toList());
    }

    @Override
    public RoomPayment getPaymentOfAllStudentsInRoom(Integer id) {
        DetailRoomDto detailRoomDto = getRoomById(id);
        List<StudentBill> studentBills = detailRoomDto.getStudents().stream()
                .map(studentDto -> studentService.getInfoMoneyRoomPaymentDto(studentDto.getId()))
                .collect(Collectors.toList());
        return new RoomPayment(id, detailRoomDto.getName(), studentBills);
    }

    @Override
    public int countRoom() {
        return getAllDetailRoomDto().size();
    }

    @Override
    public int countRemainingRoom() {
        return getAllRemainingRoomDto("").size();
    }

}
