package com.managedormitory.services.impl;

import com.managedormitory.converters.RoomConvertToRoomDto;
import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.helper.RoomExcelHelper;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.DetailRoomDto;
import com.managedormitory.models.dto.PaginationRoom;
import com.managedormitory.models.dto.RoomDto;
import com.managedormitory.models.dto.StudentDto;
import com.managedormitory.models.filter.RoomFilterDto;
import com.managedormitory.repositories.RoomRepository;
import com.managedormitory.repositories.custom.RoomRepositoryCustom;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
import com.managedormitory.services.RoomService;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
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
    private RoomConvertToRoomDto roomConvertToRoomDto;

    @Autowired
    private StudentRepositoryCustom studentRepositoryCustom;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<RoomDto> getAllRoomDto() {
        List<Room> a = getAllRooms();
        return roomConvertToRoomDto.convert(a);
    }

    @Override
    public List<DetailRoomDto> getAllDetailRoomDto() {
        List<Room> rooms = getAllRooms();
        List<RoomDto> roomDtos = roomRepositoryCustom.getAllRoomByTime();
        List<DetailRoomDto> detailRoomDtos = new ArrayList<>();
        List<Integer> roomDtosDdList = roomDtos.stream().mapToInt(RoomDto::getId).boxed().collect(Collectors.toList());
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            DetailRoomDto detailRoomDto = new DetailRoomDto();
            detailRoomDto.setRoomId(room.getId());
            detailRoomDto.setRoomName(room.getName());
            List<StudentDto> studentDtos = studentConvertToStudentDto.convert(room.getStudents());
            studentDtos.sort(Comparator.comparing(StudentDto::getId));
            detailRoomDto.setStudents(studentDtos);
            detailRoomDto.setQuantityStudent(room.getQuantityStudent());
            if (room.getTypeRoom() == null) {
                detailRoomDto.setTypeRoom(null);
            } else {
                detailRoomDto.setTypeRoom(room.getTypeRoom());
            }
            detailRoomDto.setCampusName(room.getCampus().getName());
            detailRoomDto.setUserManager(room.getCampus().getUserManager().getFullName());
            detailRoomDto.setPriceRoom(room.getPriceList().getPrice());
//            if (room.getQuantityStudent() == 0) {
//                detailRoomDto.setPriceWater(null);
//                detailRoomDto.setPriceVehicle(null);
//            } else {
//                System.out.println("room:" +room);
//                detailRoomDto.setPriceWater(room.getPriceList().getWaterBill().getPriceList().getPrice());
//                detailRoomDto.setPriceVehicle(room.getPriceList().getVehicleBill().getPriceList().getPrice());
//            }
            if (roomDtosDdList.contains(room.getId())) {
                detailRoomDto.setIsPayRoom(true);
                detailRoomDto.setIsPayPowerBill(true);
                detailRoomDto.setIsPayWaterBill(true);
                detailRoomDto.setIsPayVehicleBill(true);
            } else {
                detailRoomDto.setIsPayRoom(false);
                detailRoomDto.setIsPayPowerBill(false);
                detailRoomDto.setIsPayWaterBill(false);
                detailRoomDto.setIsPayVehicleBill(false);
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
                    .filter(detailRoomDto -> detailRoomDto.getUserManager().toLowerCase().matches(searchText)
                            || detailRoomDto.getRoomName().toLowerCase().matches(searchText))
                    .collect(Collectors.toList());
        }
        if (roomFilterDto.getQuantityStudent() != null && roomFilterDto.getQuantityStudent() >= 0) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getQuantityStudent().equals(roomFilterDto.getQuantityStudent()))
                    .collect(Collectors.toList());
        }
        if (roomFilterDto.getTypeRoom() != null && !roomFilterDto.getTypeRoom().equals("All")) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getTypeRoom() != null && detailRoomDto.getTypeRoom().getName().toLowerCase().equals(roomFilterDto.getTypeRoom().toLowerCase()))
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
                .filter(detailRoomDto -> detailRoomDto.getRoomId() == id)
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + " " + b);
                }).get();
    }

    @Override
    public ByteArrayInputStream exportExcelRooms() throws IOException {
        List<RoomDto> roomDtos = getAllRoomDto();
        RoomExcelHelper roomExcelHelper = new RoomExcelHelper(roomDtos);
        try {
            ByteArrayInputStream inputStream = roomExcelHelper.export();
            return inputStream;
        } catch (IOException e) {
            throw new IOException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStudentInRoom(Integer roomId, StudentDto studentDtoNew) throws NotFoundException {
        DetailRoomDto currentDetailRoomDto = getRoomById(roomId);
        List<StudentDto> studentDtos = currentDetailRoomDto.getStudents();
        int resultStudent = 0;
        int resultQuantity = 0;
        int resultTypeRoom = 0;
        for (StudentDto studentDto : studentDtos) {
            if (studentDto.getId() == studentDtoNew.getId()) {
                resultStudent = studentRepositoryCustom.updateRoomIdOfStudent(studentDtoNew.getId(), null);
                resultQuantity = roomRepositoryCustom.updateQuantityStudent(roomId);
                resultTypeRoom = roomRepositoryCustom.updateTypeRoom(roomId, currentDetailRoomDto);
            }
        }
        if (resultStudent > 0 && resultQuantity > 0 && resultTypeRoom > 0) {
            return true;
        }
        throw new NotFoundException("Cannot implement the method");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DetailRoomDto updateTypeRoom(Integer id, DetailRoomDto room) {
        if (roomRepositoryCustom.updateTypeRoom(id, room) > 0) {
            return getRoomById(id);
        }
        return null;
    }
}
