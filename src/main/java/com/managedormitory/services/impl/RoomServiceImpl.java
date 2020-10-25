package com.managedormitory.services.impl;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.DetailRoomDto;
import com.managedormitory.models.dto.PaginationRoom;
import com.managedormitory.models.dto.RoomDto;
import com.managedormitory.models.filter.RoomFilterDto;
import com.managedormitory.repositories.RoomRepository;
import com.managedormitory.repositories.custom.RoomRepositoryCustom;
import com.managedormitory.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomRepositoryCustom roomRepositoryCustom;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<DetailRoomDto> getAllRoomDto() {
        List<Room> rooms = getAllRooms();
        List<RoomDto> roomDtos = roomRepositoryCustom.getAllRoomByTime();
        List<DetailRoomDto> detailRoomDtos = new ArrayList<>();
        List<Integer> roomDtosDdList = roomDtos.stream().mapToInt(roomDto -> roomDto.getId()).boxed().collect(Collectors.toList());
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            DetailRoomDto detailRoomDto = new DetailRoomDto();
            detailRoomDto.setRoomId(room.getId());
            detailRoomDto.setRoomName(room.getName());
            detailRoomDto.setStudents(room.getStudents());
            detailRoomDto.setQuantityStudent(room.getQuantityStudent());
            if (room.getTypeRoom() == null) {
                detailRoomDto.setTypeRoom(null);
            } else {
                detailRoomDto.setTypeRoom(room.getTypeRoom().getName());
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
        List<DetailRoomDto> detailRoomDtos = getAllRoomDto();
        if (roomFilterDto.getCampusName() != null) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getCampusName().toLowerCase().equals(roomFilterDto.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (roomFilterDto.getRoomNameOrUserManager() != null) {
            String searchText = roomFilterDto.getRoomNameOrUserManager().toLowerCase();
            if (!searchText.equals("")) {
                detailRoomDtos = detailRoomDtos.stream()
                        .filter(detailRoomDto -> detailRoomDto.getUserManager().toLowerCase().equals(searchText)
                                || detailRoomDto.getRoomName().toLowerCase().equals(searchText))
                        .collect(Collectors.toList());
            }
        }
        if (roomFilterDto.getQuantityStudent() != null && roomFilterDto.getQuantityStudent() >= 0) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getQuantityStudent().equals(roomFilterDto.getQuantityStudent()))
                    .collect(Collectors.toList());
        }
        if (roomFilterDto.getTypeRoom() != null) {
            detailRoomDtos = detailRoomDtos.stream()
                    .filter(detailRoomDto -> detailRoomDto.getTypeRoom().toLowerCase().equals(roomFilterDto.getTypeRoom().toLowerCase()))
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
        List<DetailRoomDto> detailRoomDtos = getAllRoomDto();
        List<DetailRoomDto> detailRoomDtoById = detailRoomDtos.stream().filter(detailRoomDto -> detailRoomDto.getRoomId().equals(id)).collect(Collectors.toList());
        if (detailRoomDtoById.size() == 0) {
            throw new NotFoundException("Cannot find Room Id: " + id);
        }
        return detailRoomDtoById.get(0);
    }
}
