package com.managedormitory.services.impl;

import com.managedormitory.models.dao.PowerBill;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.filter.PowerBillFilter;
import com.managedormitory.repositories.PowerBillRepository;
import com.managedormitory.repositories.custom.PowerBillRepositoryCustom;
import com.managedormitory.services.PowerBillService;
import com.managedormitory.services.RoomService;
import com.managedormitory.utils.LimitedPower;
import com.managedormitory.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PowerBillServiceImpl implements PowerBillService {
    @Autowired
    private PowerBillRepository powerBillRepository;

    @Autowired
    private PowerBillRepositoryCustom powerBillRepositoryCustom;

    @Autowired
    private RoomService roomService;

    @Override
    public List<PowerBill> getAllPowerBills() {
        return powerBillRepository.findAll();
    }

    @Override
    public List<PowerBillDetail> getAllDetailPowerBills() {
        List<Room> rooms = roomService.getAllRooms();
        List<PowerBillDto> powerBillDtos = powerBillRepositoryCustom.getAllPowerBillByTime();
        List<PowerBillDetail> powerBillDetails = new ArrayList<>();
        List<Integer> powerBillDetailIdList = powerBillDtos.stream().mapToInt(PowerBillDto::getRoomId).boxed().collect(Collectors.toList());
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            PowerBillDetail powerBillDetail = new PowerBillDetail();
            if (powerBillDetailIdList.contains(room.getId())) {
                PowerBillDto powerBillDto = powerBillDtos.stream()
                        .filter(pb -> pb.getRoomId() == room.getId())
                        .findFirst().get();
                powerBillDetail.setBillId(powerBillDto.getBillId());
                powerBillDetail.setStartDate(powerBillDto.getStartDate());
                powerBillDetail.setEndDate(powerBillDto.getEndDate());
                powerBillDetail.setNumberOfPowerBegin(powerBillDto.getNumberOfPowerBegin());
                powerBillDetail.setNumberOfPowerEnd(powerBillDto.getNumberOfPowerEnd());
                powerBillDetail.setNumberOfPowerUsed(powerBillDto.getNumberOfPowerUsed());
                powerBillDetail.setNumberOfMoneyMustPay(calculatePowerBill(powerBillDto));
                powerBillDetail.setPriceAKWH(powerBillDto.getPriceAKWH());
                powerBillDetail.setPay(powerBillDto.isPay());
            } else {
                powerBillDetail = new PowerBillDetail();
            }
            powerBillDetail.setRoomDto(new RoomDto(room));
            powerBillDetails.add(powerBillDetail);
        }
        return powerBillDetails;
    }

    @Override
    public PaginationPowerBill paginationGetAllPowerBills(PowerBillFilter powerBillFilter, int skip, int take) {
        List<PowerBillDetail> powerBillDetails = getAllDetailPowerBills();
        if (powerBillFilter.getCampusName() != null) {
            powerBillDetails = powerBillDetails.stream()
                    .filter(powerBillDetail -> powerBillDetail.getRoomDto() != null && powerBillDetail.getRoomDto().getCampusName().toLowerCase().equals(powerBillFilter.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (powerBillFilter.getRoomName() != null) {
            powerBillDetails = powerBillDetails.stream()
                    .filter(powerBillDetail -> powerBillDetail.getRoomDto() != null && powerBillDetail.getRoomDto().getName().toLowerCase().equals(powerBillFilter.getRoomName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        int total = powerBillDetails.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<PowerBillDetail>> powerBillMap = new HashMap<>();
        powerBillMap.put("data", powerBillDetails.subList(skip, lastElement));
        return new PaginationPowerBill(powerBillMap, total);
    }

    @Override
    public float calculatePowerBill(PowerBillDto powerBillDto) {
        float pricePowerAKWH = powerBillDto.getPriceAKWH();
        long numberOfPowerUsed = powerBillDto.getNumberOfPowerEnd() - powerBillDto.getNumberOfPowerBegin();
        float money = 0;
        if (numberOfPowerUsed <= LimitedPower.LOW_POWER) {
            money = pricePowerAKWH * LimitedPower.LOW_POWER;
        } else if (LimitedPower.LOW_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.MIDDLE_POWER) {
            money = pricePowerAKWH * LimitedPower.MIDDLE_POWER_FINES;
        } else if (LimitedPower.MIDDLE_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * LimitedPower.HIGH_POWER_FINES;
        } else if (numberOfPowerUsed > LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * LimitedPower.HIGHER_POWER_FINES;
        }
        return money;
    }

    @Override
    public PowerBillDetail getAPowerBill(Integer roomId) {
        List<PowerBillDetail> powerBillDtos = getAllDetailPowerBills();
        return powerBillDtos.stream().filter(powerBillDto -> powerBillDto.getRoomDto().getId() == roomId)
                .findFirst().orElse(null);
    }
}
