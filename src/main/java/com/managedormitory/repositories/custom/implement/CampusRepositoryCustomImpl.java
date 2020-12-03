package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.UserDto;
import com.managedormitory.repositories.custom.CampusRepositoryCustom;
import org.springframework.stereotype.Component;

@Component
public class CampusRepositoryCustomImpl implements CampusRepositoryCustom {
    @Override
    public int updateCampus(Integer id, UserDto userDto) {
        return 0;
    }
}
