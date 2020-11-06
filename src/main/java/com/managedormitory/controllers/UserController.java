package com.managedormitory.controllers;

import com.managedormitory.models.dto.pagination.PaginationUser;
import com.managedormitory.models.dto.UserDto;
import com.managedormitory.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public PaginationUser filterUser(@RequestParam(required = false) String fullName, @RequestParam int skip, @RequestParam int take) {
        return userService.paginationGetAllUsers(fullName, skip, take);
    }

    @GetMapping("/{id}")
    public UserDto getDetailUser(@PathVariable Integer id){
        try {
            return userService.getUserById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
