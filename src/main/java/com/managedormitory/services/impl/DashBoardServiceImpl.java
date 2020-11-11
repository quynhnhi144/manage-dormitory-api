package com.managedormitory.services.impl;

import com.managedormitory.models.dto.DashBoard;
import com.managedormitory.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    private UserService userService;

    @Autowired
    private CampusService campusService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private StudentService studentService;

    @Override
    public DashBoard getDashBoard() {
        return new DashBoard(userService.countUser(), campusService.countCampus(), roomService.countRoom(), vehicleService.countVehicle(), studentService.countStudent(), roomService.countRemainingRoom());
    }
}
