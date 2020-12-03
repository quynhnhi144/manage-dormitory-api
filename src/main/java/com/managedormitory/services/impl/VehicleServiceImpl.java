package com.managedormitory.services.impl;

import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.helper.PowerBillExcelHelper;
import com.managedormitory.models.dao.PriceList;
import com.managedormitory.models.dao.Vehicle;
import com.managedormitory.models.dao.VehicleBill;
import com.managedormitory.models.dao.VehicleLeft;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.vehicle.*;
import com.managedormitory.models.dto.pagination.PaginationVehicle;
import com.managedormitory.models.filter.VehicleFilter;
import com.managedormitory.repositories.PriceListRepository;
import com.managedormitory.repositories.VehicleLeftRepository;
import com.managedormitory.repositories.VehicleRepository;
import com.managedormitory.repositories.custom.DetailRoomRepositoryCustom;
import com.managedormitory.repositories.custom.RoomRepositoryCustom;
import com.managedormitory.repositories.custom.VehicleBillRepositoryCustom;
import com.managedormitory.repositories.custom.VehicleRepositoryCustom;
import com.managedormitory.services.StudentService;
import com.managedormitory.services.VehicleService;
import com.managedormitory.utils.CalculateMoney;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service


public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleRepositoryCustom vehicleRepositoryCustom;

    @Autowired
    private StudentConvertToStudentDto studentConvertToStudentDto;

    @Autowired
    private VehicleLeftRepository vehicleLeftRepository;

    @Autowired
    private VehicleBillRepositoryCustom vehicleBillRepositoryCustom;

    @Autowired
    private RoomRepositoryCustom roomRepositoryCustom;

    @Autowired
    private PriceListRepository priceListRepository;

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<VehicleLeft> getAllVehicleLeft() {
        return vehicleLeftRepository.findAll();
    }

    @Override
    public List<VehicleDetailDto> getAllVehicleDto() {
        List<Vehicle> vehicles = getAllVehicles();
        List<VehicleDto> vehicleDtos = vehicleRepositoryCustom.getAllVehicleByTime();
        List<VehicleDetailDto> vehicleDtosDetail = new ArrayList<>();
        List<Integer> vehicleDtoIdList = vehicleDtos.stream().mapToInt(VehicleDto::getId).boxed().collect(Collectors.toList());
        List<Integer> vehicleLeftIds = getAllVehicleLeft().stream().mapToInt(VehicleLeft::getId).boxed().collect(Collectors.toList());
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            VehicleDetailDto vehicleDetailDto = new VehicleDetailDto();
            vehicleDetailDto.setId(vehicle.getId());
            vehicleDetailDto.setLicensePlates(vehicle.getLicensePlates());
            vehicleDetailDto.setTypeVehicle(vehicle.getTypeVehicleId());
            vehicleDetailDto.setStudentDto(studentConvertToStudentDto.convert(vehicle.getStudentId()));
            if (vehicle.getStudentId().getRoom() == null) {
                vehicleDetailDto.setRoomName(null);
                vehicleDetailDto.setCampusName(null);
                vehicleDetailDto.setUserManager(null);
            } else {
                vehicleDetailDto.setRoomName(vehicle.getStudentId().getRoom().getName());
                vehicleDetailDto.setCampusName(vehicle.getStudentId().getRoom().getCampus().getName());
                if (vehicle.getStudentId().getRoom().getCampus().getUserManager() == null) {
                    vehicleDetailDto.setUserManager(null);
                } else {
                    vehicleDetailDto.setUserManager(vehicle.getStudentId().getRoom().getCampus().getUserManager().getFullName());
                }
            }

            if (vehicleDtoIdList.contains(vehicle.getId())) {
                vehicleDetailDto.setPayVehicleBill(true);
            } else {
                vehicleDetailDto.setPayVehicleBill(false);
            }

            if (vehicleLeftIds.contains(vehicle.getId())) {
                vehicleDetailDto.setActive(false);
            } else {
                vehicleDetailDto.setActive(true);
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
            vehicleDetailDtos = vehicleDetailDtos.stream().filter(vehicleDto -> vehicleDto.getTypeVehicle().getName().equals(vehicleFilter.getTypeVehicle().toLowerCase()))
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VehicleDetailDto updateVehicle(Integer id, VehicleDetailDto vehicleDetailDto) {
        if (vehicleRepositoryCustom.updateVehicle(id, vehicleDetailDto) <= 0) {
            throw new BadRequestException("Cannot excute");
        }
        return getVehicleById(id);
    }

    @Override
    public VehicleMoveDto getInfoMovingVehicle(Integer vehicleId) {
        LocalDate currentDate = LocalDate.now();
        VehicleDetailDto vehicleDetailDto = getVehicleById(vehicleId);
        float remainingMoneyOfVehicle = 0;
        VehicleBillDto vehicleBillDto = vehicleBillRepositoryCustom.getVehicleBillRecentlyByVehicleId(vehicleId).orElse(null);
        if (vehicleBillDto != null) {
            remainingMoneyOfVehicle = CalculateMoney.calculateRemainingMoney(currentDate, DateUtil.getLDateFromSDate(vehicleBillDto.getEndDate()), vehicleBillDto.getPrice());
        }
        return new VehicleMoveDto(vehicleId, vehicleDetailDto.getLicensePlates(), vehicleDetailDto.getStudentDto().getName(), currentDate, remainingMoneyOfVehicle, vehicleBillDto.getStudentId());
    }

    @Override
    public VehicleBillDto VehicleBillInfoForNewVehicle(Integer studentId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDateVehicleBill = LocalDate.now();
        LocalDate endDateVehicleBill = LocalDate.now();

        float remainingMoneyOfVehicle = 0;
        PriceList priceList = priceListRepository.findById(3).orElse(null);
        RoomBillDto roomBillDto = roomRepositoryCustom.getDetailRoomRecently(studentId).orElse(null);
        if (roomBillDto != null) {
            if (DateUtil.getLDateFromSDate(roomBillDto.getEndDate()).isBefore(currentDate)) {
                startDateVehicleBill = currentDate;
                endDateVehicleBill = DateUtil.getLDateFromSDate(roomBillDto.getEndDate()).plus(30, ChronoUnit.DAYS);
            } else {
                startDateVehicleBill = currentDate;
                endDateVehicleBill = DateUtil.getLDateFromSDate(roomBillDto.getEndDate());
            }
        } else {
            startDateVehicleBill = currentDate;
            endDateVehicleBill = currentDate.plus(30, ChronoUnit.DAYS);
        }
        remainingMoneyOfVehicle = Math.abs(CalculateMoney.calculateRemainingMoney(startDateVehicleBill, endDateVehicleBill, priceList.getPrice()));

        return new VehicleBillDto(null, roomBillDto.getStudentName(), roomBillDto.getStudentId(), DateUtil.getSDateFromLDate(startDateVehicleBill), DateUtil.getSDateFromLDate(endDateVehicleBill), remainingMoneyOfVehicle, roomBillDto.getRoomId(), null);
    }

    @Override
    public ByteArrayInputStream exportExcel() throws IOException {
        List<VehicleDetailDto> vehicleDetailDtos = getAllVehicleDto();
        PowerBillExcelHelper<VehicleDetailDto> powerBillExcelHelper = new PowerBillExcelHelper<>(vehicleDetailDtos);
        powerBillExcelHelper.writeHeaderLine(StringUtil.HEADER_VEHICLES, StringUtil.SHEET_VEHICLE);
        powerBillExcelHelper.writeDataLines(vehicleDetailDto -> {
            int rowCount = 1;
            CellStyle style = powerBillExcelHelper.getWorkbook().createCellStyle();
            XSSFFont font = powerBillExcelHelper.getWorkbook().createFont();
            font.setFontHeight(14);
            style.setFont(font);

            for (VehicleDetailDto vehicleDetailDtoCurrent : vehicleDetailDtos) {
                Row row = powerBillExcelHelper.getSheet().createRow(rowCount++);
                int columnCount = 0;
                powerBillExcelHelper.createCell(row, columnCount++, vehicleDetailDtoCurrent.getId(), style);
                powerBillExcelHelper.createCell(row, columnCount++, vehicleDetailDtoCurrent.getLicensePlates(), style);
                powerBillExcelHelper.createCell(row, columnCount++, vehicleDetailDtoCurrent.getTypeVehicle().getName(), style);
                powerBillExcelHelper.createCell(row, columnCount++, vehicleDetailDtoCurrent.getStudentDto().getName(), style);
                powerBillExcelHelper.createCell(row, columnCount++, vehicleDetailDtoCurrent.isPayVehicleBill() ? "x" : "--", style);
                powerBillExcelHelper.createCell(row, columnCount++, vehicleDetailDtoCurrent.isActive() ? "x" : "--", style);
            }
        });
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        powerBillExcelHelper.getWorkbook().write(outputStream);
        powerBillExcelHelper.getWorkbook().close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addVehicleLeft(VehicleMoveDto vehicleMoveDto) {
        int resultVehicleLeft = vehicleRepositoryCustom.addVehicleLeft(vehicleMoveDto);
        if (resultVehicleLeft > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot implement method!!!");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addVehicle(VehicleNew vehicleNew) {
        int resultAddVehicle = vehicleRepositoryCustom.addVehicle(vehicleNew);
        if (resultAddVehicle > 0) {
            VehicleDetailDto vehicleDetailDto = getAllVehicleDto().get(getAllVehicleDto().size() - 1);

            VehicleBillDto vehicleBillDto = vehicleNew.getVehicleBillDto();
            vehicleBillDto.setVehicleId(vehicleDetailDto.getId());
            int resultAddVehicleBill = vehicleBillRepositoryCustom.addVehicleBillRepository(vehicleBillDto);
            if (resultAddVehicleBill > 0) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int countVehicle() {
        return getAllVehicleDto().size();
    }
}
