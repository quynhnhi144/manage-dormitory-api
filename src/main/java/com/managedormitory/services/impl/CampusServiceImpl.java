package com.managedormitory.services.impl;

import com.managedormitory.models.dao.Campus;
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

    @Override
    public List<Campus> getAllCampuses() {
        return campusRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
