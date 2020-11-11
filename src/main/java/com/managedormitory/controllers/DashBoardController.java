package com.managedormitory.controllers;

import com.managedormitory.models.dto.DashBoard;
import com.managedormitory.services.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("api/dashboard")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping
    public DashBoard getDashBoard() {
        return dashBoardService.getDashBoard();
    }

}
