package com.managedormitory.services.impl;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.Vehicle;
import com.managedormitory.models.dto.PaginationVehicle;
import com.managedormitory.models.dto.VehicleDto;
import com.managedormitory.models.filter.VehicleFilter;
import com.managedormitory.repositories.VehicleRepository;
import com.managedormitory.repositories.custom.VehicleRepositoryCustom;
import com.managedormitory.services.VehicleService;
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
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleRepositoryCustom vehicleRepositoryCustom;

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<VehicleDto> getAllVehicleDto() {
        List<Vehicle> vehicles = getAllVehicles();
        List<VehicleDto> vehicleDtos = vehicleRepositoryCustom.getAllVehicleByTime();
        List<VehicleDto> vehicleDtosDetail = new ArrayList<>();
        List<Integer> vehicleDtoIdList = vehicleDtos.stream().mapToInt(VehicleDto::getId).boxed().collect(Collectors.toList());
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            VehicleDto vehicleDto = new VehicleDto();
            vehicleDto.setId(vehicle.getId());
            vehicleDto.setLicensePlates(vehicle.getLicensePlates());
            vehicleDto.setTypeVehicle(vehicle.getTypeVehicleId().getName());
            vehicleDto.setStudentId(vehicle.getStudentId().getId());
            vehicleDto.setStudentName(vehicle.getStudentId().getName());
            vehicleDto.setRoomName(vehicle.getStudentId().getRoom().getName());
            vehicleDto.setCampusName(vehicle.getStudentId().getRoom().getCampus().getName());
            vehicleDto.setUserManager(vehicle.getStudentId().getRoom().getCampus().getUserManager().getFullName());
            if (vehicleDtoIdList.contains(vehicle.getId())) {
                vehicleDto.setIsPayVehicleBill(true);
            } else {
                vehicleDto.setIsPayVehicleBill(false);
            }

            vehicleDtosDetail.add(vehicleDto);
        }
        return vehicleDtosDetail;
    }

    @Override
    public PaginationVehicle paginationGetAllVehicles(VehicleFilter vehicleFilter, int skip, int take) {
        List<VehicleDto> vehicleDtos = getAllVehicleDto();
        if (vehicleFilter.getCampusName() != null) {
            vehicleDtos = vehicleDtos.stream().filter(vehicleDto -> vehicleDto.getCampusName().toLowerCase().equals(vehicleFilter.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (vehicleFilter.getLicensePlates() != null) {
            if (!vehicleFilter.getLicensePlates().equals("")) {
                vehicleDtos = vehicleDtos.stream().filter(vehicleDto -> vehicleDto.getLicensePlates().toLowerCase().equals(vehicleFilter.getLicensePlates().toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        if (vehicleFilter.getTypeVehicle() != null) {
            if (!vehicleFilter.getTypeVehicle().equals("All")) {
                vehicleDtos = vehicleDtos.stream().filter(vehicleDto -> vehicleDto.getTypeVehicle().toLowerCase().equals(vehicleFilter.getTypeVehicle().toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        int total = vehicleDtos.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<VehicleDto>> vehicleDtoMap = new HashMap<>();
        vehicleDtoMap.put("data", vehicleDtos.subList(skip, lastElement));
        return new PaginationVehicle(vehicleDtoMap, total);
    }

    @Override
    public VehicleDto getVehicleById(Integer id) {
        List<VehicleDto> vehicleDtos = getAllVehicleDto();
        List<VehicleDto> vehicleById = vehicleDtos.stream()
                .filter(vehicleDto -> vehicleDto.getId().equals(id))
                .collect(Collectors.toList());

        if (vehicleById.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return vehicleById.get(0);
    }
}
