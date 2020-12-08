package com.managedormitory.services.impl;

import com.managedormitory.converters.CampusCovertToCampusDto;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.User;
import com.managedormitory.models.dto.CampusDto;
import com.managedormitory.models.dto.UserUpdate;
import com.managedormitory.models.dto.pagination.PaginationUser;
import com.managedormitory.models.dto.UserDto;
import com.managedormitory.repositories.UserRepository;
import com.managedormitory.repositories.custom.UserRepositoryCustom;
import com.managedormitory.services.UserService;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service


public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryCustom userRepositoryCustom;

    @Autowired
    private CampusCovertToCampusDto campusCovertToCampusDto;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<UserDto> getAllUserDto() {
        List<User> users = getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setFullName(user.getFullName());
            userDto.setBirthday(DateUtil.getLDateFromSDate(user.getBirthday()));
            userDto.setEmail(user.getEmail());
            userDto.setAddress(user.getAddress());
            userDto.setPhone(user.getPhone());
            userDto.setCampuses(campusCovertToCampusDto.convert(user.getCampuses()));
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public PaginationUser paginationGetAllUsers(String fullName, int skip, int take) {
        List<UserDto> userDtos = getAllUserDto();
        if (fullName != null && !fullName.equals("")) {
            String searchText = fullName.toLowerCase() + StringUtil.DOT_STAR;
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
        if (userDtoIdList.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return userDtoIdList.get(0);
    }

    @Override
    public UserDto getProfile(String username) {
        List<UserDto> userDtos = getAllUserDto();
        List<UserDto> userDtoIdList = userDtos.stream()
                .filter(userDto -> userDto.getUsername().equals(username))
                .collect(Collectors.toList());
        if (userDtoIdList.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + username);
        }
        return userDtoIdList.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDto updateUser(Integer id, UserUpdate userUpdate) {
        User user = userRepositoryCustom.getUser(id).orElse(null);
        int resultUpdateUser = 0;

        if (user == null) {
            throw new BadRequestException("Cannot update user!!!");
        } else {
            if (userUpdate.getOldPassword() == null && userUpdate.getNewPassword() == null) {
                resultUpdateUser = userRepositoryCustom.updateUserWithoutPassword(id, userUpdate);
            } else if (userUpdate.getOldPassword() != null && userUpdate.getNewPassword() != null && encoder.matches(userUpdate.getOldPassword(), user.getPassword())) {
                resultUpdateUser = userRepositoryCustom.updateUser(id, userUpdate);
            } else {
                throw new BadRequestException("Mat khau sai!!!");
            }
            int resultUpdateCampus = userRepositoryCustom.updateCampus(userUpdate);

            if (resultUpdateUser > 0 && resultUpdateCampus > 0) {
                return userUpdate.getUserDto();
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteUser(Integer id) {
        UserDto userDto = getUserById(id);

        UserUpdate userUpdate = new UserUpdate(userDto, null, null);
        List<CampusDto> campusDtos = userUpdate.getUserDto().getCampuses();
        long count = campusDtos.stream()
                .map(campusDto -> userRepositoryCustom.deleteCampusForDeleteUser(campusDto.getId()))
                .filter(result -> result > 0)
                .count();

        int resultDeleteUser = userRepositoryCustom.deleteUser(id);
        if (count == campusDtos.size() && resultDeleteUser > 0) {
            return 1;
        } else {
            throw new BadRequestException("Khong the xoa nhan vien");
        }
    }

    @Override
    public int countUser() {
        return getAllUserDto().size();
    }
}
