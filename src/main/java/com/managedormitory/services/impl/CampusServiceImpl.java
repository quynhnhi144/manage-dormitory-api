package com.managedormitory.services.impl;

import com.managedormitory.converters.CampusCovertToCampusDto;
import com.managedormitory.models.dao.Campus;
import com.managedormitory.models.dto.CampusDto;
import com.managedormitory.repositories.CampusRepository;
import com.managedormitory.services.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusServiceImpl implements CampusService {
    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private CampusCovertToCampusDto campusCovertToCampusDto;

    @Override
    public List<CampusDto> getAllCampuses() {

        return campusCovertToCampusDto.convert(campusRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    @Override
    public int countCampus() {
        return getAllCampuses().size();
    }
}
