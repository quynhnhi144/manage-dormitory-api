package com.managedormitory.services;

import com.managedormitory.message.request.SignUpForm;
import com.managedormitory.models.dao.User;
import com.managedormitory.models.dto.UserUpdate;
import com.managedormitory.models.dto.pagination.PaginationUser;
import com.managedormitory.models.dto.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    List<UserDto> getAllUserDto();

    PaginationUser paginationGetAllUsers(String userName, int skip, int take);

    UserDto getUserById(Integer id);

    UserDto getProfile(String username);

    UserDto updateUser(Integer id, UserUpdate userUpdate);

    int deleteUser(Integer id);

    int countUser();
}
