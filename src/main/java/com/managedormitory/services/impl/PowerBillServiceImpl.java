package com.managedormitory.services.impl;

import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dao.PowerBill;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.MessageResponse;
import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.filter.PowerBillFilter;
import com.managedormitory.repositories.PowerBillRepository;
import com.managedormitory.repositories.PriceListRepository;
import com.managedormitory.repositories.custom.PowerBillRepositoryCustom;
import com.managedormitory.services.PowerBillService;
import com.managedormitory.services.RoomService;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.LimitedPower;
import com.managedormitory.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private StudentConvertToStudentDto studentConvertToStudentDto;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public List<PowerBillDetail> getAllDetailPowerBills(LocalDate date) {
        List<Room> rooms = roomService.getAllRooms();
        List<PowerBillDto> powerBillDtos = powerBillRepositoryCustom.getAllPowerBillByTime(date);
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
            powerBillDetail.setDetailRoomDto(new DetailRoomDto(room, studentConvertToStudentDto));
            powerBillDetails.add(powerBillDetail);
        }
        return powerBillDetails;
    }

    @Override
    public PaginationPowerBill paginationGetAllPowerBills(PowerBillFilter powerBillFilter, LocalDate date, int skip, int take) {
        List<PowerBillDetail> powerBillDetails = getAllDetailPowerBills(date);
        if (powerBillFilter.getCampusName() != null) {
            powerBillDetails = powerBillDetails.stream()
                    .filter(powerBillDetail -> powerBillDetail.getDetailRoomDto() != null && powerBillDetail.getDetailRoomDto().getCampusName().toLowerCase().equals(powerBillFilter.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (powerBillFilter.getRoomName() != null && !powerBillFilter.getRoomName().equals("")) {
            powerBillDetails = powerBillDetails.stream()
                    .filter(powerBillDetail -> powerBillDetail.getDetailRoomDto() != null && powerBillDetail.getDetailRoomDto().getName().toLowerCase().equals(powerBillFilter.getRoomName().toLowerCase()))
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
            money = pricePowerAKWH * numberOfPowerUsed;
        } else if (LimitedPower.LOW_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.MIDDLE_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.MIDDLE_POWER_FINES;
        } else if (LimitedPower.MIDDLE_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.HIGH_POWER_FINES;
        } else if (numberOfPowerUsed > LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.HIGHER_POWER_FINES;
        }
        return money;
    }

    @Override
    public PowerBillDetail getAPowerBill(LocalDate date, Integer roomId) {
        List<PowerBillDetail> powerBillDtos = getAllDetailPowerBills(date);
        return powerBillDtos.stream().filter(powerBillDto -> powerBillDto.getDetailRoomDto().getId() == roomId)
                .findFirst().orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PowerBillDetail updatePowerBill(Integer roomId, PowerBillDetail powerBillDetail) throws BadRequestException{
        if (powerBillRepositoryCustom.updatePowerBill(roomId, powerBillDetail) <= 0) {
            throw new BadRequestException("Cannot implement update");
        }
        return powerBillDetail;
    }

    @Override
    public void sendMail(String text, String subject, StudentDto studentDto, JavaMailSender javaMailSender,MimeMessage message, MimeMessageHelper helper) {
        try {
            helper.setTo(studentDto.getEmail());
            helper.setSubject(subject);
            helper.setText(text);
            String path = "/home/nhile/Downloads/link.txt";

            // Attachment
            FileSystemResource file = new FileSystemResource(new File(path));
            helper.addAttachment("link", file);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
