package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface CampusRepositoryCustom {
    int updateCampus(Integer id, UserDto userDto);
}
