package com.managedormitory.controllers;

import com.managedormitory.converters.DetailRoomMapDBToDetailRoomDtoConverter;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dto.*;
import com.managedormitory.repositories.custom.DetailRoomRepository;
import com.managedormitory.repositories.custom.RoomCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Query;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("api/rooms")
public class RoomController {

    @Autowired
    private RoomCustomRepository roomCustomRepository;

    @Autowired
    private DetailRoomRepository detailRoomRepository;

    @Autowired
    private DetailRoomMapDBToDetailRoomDtoConverter detailRoomDtoConverter;

    @GetMapping
    public PaginationRoom filterRoom(@RequestParam(required = false) String campusName, @RequestParam(required = false) String userManager, @RequestParam(required = false) String typeRoom, @RequestParam(required = false) Integer quantityStudent, @RequestParam int skip, @RequestParam int take) {
        RoomFilterDto roomFilterDto = RoomFilterDto.builder().campusName(campusName).userManager(userManager).typeRoom(typeRoom).quantityStudent(quantityStudent).build();
        Query query = roomCustomRepository.filterRoom(roomFilterDto);
        Query queryTemp = roomCustomRepository.filterRoom(roomFilterDto);
        int total = queryTemp.getResultList().size();
        query.setFirstResult(skip);
        query.setMaxResults(take);
        List<RoomDto> roomDtoList = query.getResultList();
        Map<String, List<RoomDto>> roomDtoMap = new HashMap<>();
        roomDtoMap.put("data", roomDtoList);

        return new PaginationRoom(roomDtoMap, total);
    }


//    @GetMapping("/{id}")
//    public Map<Integer, List<DetailRoomMapDB>> getData(@PathVariable Integer id) {
//        List<DetailRoomMapDB> list = detailRoomRepository.getDetailARoom(id);
//        System.out.println(list);
//
//        Map<Integer, List<DetailRoomMapDB>> aa = new HashMap<>();
////        for(DetailRoomMapDB yy : list){
////            if(!aa.containsKey(yy.getRoomId())){
////                List<DetailRoomMapDB> current = new ArrayList<>();
////                current.add(yy);
////                aa.put(yy.getRoomId(), current);
////            }else{
////
////            }
////        }
//        if (list.size() > 0) {
//            aa.put(list.get(0).getRoomId(), list);
//        } else {
//            throw new NotFoundException("Not Found that Id");
//        }
//        return aa;
//    }

    @GetMapping("/{id}")
    public DetailRoomDto getDetailARoom(@PathVariable Integer id) {
        List<DetailRoomMapDB> detailRoomMapDBS = detailRoomRepository.getDetailARoom(id);
        DetailRoomDto detailRoomDto = detailRoomDtoConverter.convert(detailRoomMapDBS);
        return detailRoomDto;
    }
}
