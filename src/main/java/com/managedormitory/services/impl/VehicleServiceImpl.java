package com.managedormitory.services.impl;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.Vehicle;
import com.managedormitory.models.dto.VehicleDetailDto;
import com.managedormitory.models.dto.pagination.PaginationVehicle;
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
    public List<VehicleDetailDto> getAllVehicleDto() {
        List<Vehicle> vehicles = getAllVehicles();
        List<VehicleDto> vehicleDtos = vehicleRepositoryCustom.getAllVehicleByTime();
        List<VehicleDetailDto> vehicleDtosDetail = new ArrayList<>();
        List<Integer> vehicleDtoIdList = vehicleDtos.stream().mapToInt(VehicleDto::getId).boxed().collect(Collectors.toList());
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            VehicleDetailDto vehicleDetailDto = new VehicleDetailDto();
            vehicleDetailDto.setId(vehicle.getId());
            vehicleDetailDto.setLicensePlates(vehicle.getLicensePlates());
            vehicleDetailDto.setTypeVehicle(vehicle.getTypeVehicleId().getName());
            vehicleDetailDto.setStudentId(vehicle.getStudentId().getId());
            vehicleDetailDto.setStudentName(vehicle.getStudentId().getName());
            if (vehicle.getStudentId().getRoom() == null) {
                vehicleDetailDto.setRoomName(null);
                vehicleDetailDto.setCampusName(null);
                vehicleDetailDto.setUserManager(null);
            } else {
                vehicleDetailDto.setRoomName(vehicle.getStudentId().getRoom().getName());
                vehicleDetailDto.setCampusName(vehicle.getStudentId().getRoom().getCampus().getName());
                vehicleDetailDto.setUserManager(vehicle.getStudentId().getRoom().getCampus().getUserManager().getFullName());
            }

            if (vehicleDtoIdList.contains(vehicle.getId())) {
                vehicleDetailDto.setPayVehicleBill(true);
            } else {
                vehicleDetailDto.setPayVehicleBill(false);
            }

            vehicleDtosDetail.add(vehicleDetailDto);
        }
        return vehicleDtosDetail;
    }

    @Override
    public PaginationVehicle paginationGetAllVehicles(VehicleFilter vehicleFilter, int skip, int take) {
        List<VehicleDetailDto> vehicleDetailDtos = getAllVehicleDto();
        if (vehicleFilter.getCampusName() != null) {
            vehicleDetailDtos = vehicleDetailDtos.stream().filter(vehicleDto ->
                    vehicleDto.getCampusName() != null && vehicleDto.getCampusName().toLowerCase().equals(vehicleFilter.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (vehicleFilter.getLicensePlates() != null && !vehicleFilter.getLicensePlates().equals("")) {
            vehicleDetailDtos = vehicleDetailDtos.stream().filter(vehicleDto -> vehicleDto.getLicensePlates().toLowerCase().equals(vehicleFilter.getLicensePlates().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (vehicleFilter.getTypeVehicle() != null && !vehicleFilter.getTypeVehicle().equals("All")) {
            vehicleDetailDtos = vehicleDetailDtos.stream().filter(vehicleDto -> vehicleDto.getTypeVehicle().toLowerCase().equals(vehicleFilter.getTypeVehicle().toLowerCase()))
                    .collect(Collectors.toList());
        }

        int total = vehicleDetailDtos.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<VehicleDetailDto>> vehicleDtoMap = new HashMap<>();
        vehicleDtoMap.put("data", vehicleDetailDtos.subList(skip, lastElement));
        return new PaginationVehicle(vehicleDtoMap, total);
    }

    @Override
    public VehicleDetailDto getVehicleById(Integer id) {
        List<VehicleDetailDto> vehicleDetailDtos = getAllVehicleDto();
        List<VehicleDetailDto> vehicleById = vehicleDetailDtos.stream()
                .filter(vehicleDto -> vehicleDto.getId().equals(id))
                .collect(Collectors.toList());

        if (vehicleById.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return vehicleById.get(0);
    }

    @Override
    public int countVehicle() {
        return getAllVehicleDto().size();
    }
}
