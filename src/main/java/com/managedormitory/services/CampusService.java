package com.managedormitory.services;

import com.managedormitory.models.dto.CampusDto;

import java.util.List;

public interface CampusService {
    List<CampusDto> getAllCampuses();
    int countCampus();
}
