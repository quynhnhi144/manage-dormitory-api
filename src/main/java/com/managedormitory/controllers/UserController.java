package com.managedormitory.controllers;

import com.managedormitory.models.dto.UserUpdate;
import com.managedormitory.models.dto.pagination.PaginationUser;
import com.managedormitory.models.dto.UserDto;
import com.managedormitory.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public PaginationUser filterUser(@RequestParam(required = false) String searchText, @RequestParam int skip, @RequestParam int take) {
        return userService.paginationGetAllUsers(searchText, skip, take);
    }

    @GetMapping("/{id}")
    public UserDto getDetailUser(@PathVariable Integer id) {
        try {
            return userService.getUserById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/profile/{username}")
    public UserDto getProfile(@PathVariable String username) {
        try {
            return userService.getProfile(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUserDto();
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Integer id, @RequestBody UserUpdate userUpdate) {
        return userService.updateUser(id, userUpdate);
    }

    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }
}
