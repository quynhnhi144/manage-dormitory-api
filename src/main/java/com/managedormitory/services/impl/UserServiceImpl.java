package com.managedormitory.services.impl;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.User;
import com.managedormitory.models.dto.pagination.PaginationUser;
import com.managedormitory.models.dto.UserDto;
import com.managedormitory.repositories.UserRepository;
import com.managedormitory.services.UserService;
import com.managedormitory.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "userId"));
    }

    @Override
    public List<UserDto> getAllUserDto() {
        List<User> users = getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getUserId());
            userDto.setUsername(user.getUsername());
            userDto.setFullName(user.getFullName());
            userDto.setBirthday(user.getBirthday());
            userDto.setEmail(user.getEmail());
            userDto.setAddress(user.getAddress());
            userDto.setPhone(user.getPhone());
            userDto.setCampuses(user.getCampuses());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public PaginationUser paginationGetAllUsers(String fullName, int skip, int take) {
        List<UserDto> userDtos = getAllUserDto();
        if (fullName != null && !fullName.equals("")) {
            userDtos = userDtos.stream()
                    .filter(userDto -> userDto.getFullName().toLowerCase().equals(fullName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        int total = userDtos.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<UserDto>> userMap = new HashMap<>();
        userMap.put("data", userDtos.subList(skip, lastElement));
        return new PaginationUser(userMap, total);
    }

    @Override
    public UserDto getUserById(Integer id) {
        List<UserDto> userDtos = getAllUserDto();
        List<UserDto> userDtoIdList = userDtos.stream()
                .filter(userDto -> userDto.getId().equals(id))
                .collect(Collectors.toList());
        if(userDtoIdList.size() == 0){
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return userDtoIdList.get(0);
    }

    @Override
    public int countUser() {
        return getAllUserDto().size();
    }
}
