package com.managedormitory.controllers;

import com.managedormitory.models.dto.MessageResponse;
import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.filter.PowerBillFilter;
import com.managedormitory.services.PowerBillService;
import com.managedormitory.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/powerBills")
public class PowerBillController {

    @Autowired
    private PowerBillService powerBillService;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping
    public PaginationPowerBill filterPowerBill(@RequestParam(required = false) String campusName, @RequestParam(required = false) String searchText, @RequestParam String date, @RequestParam int skip, @RequestParam int take) {
        PowerBillFilter powerBillFilter = PowerBillFilter.builder().campusName(campusName).roomName(searchText).build();
        return powerBillService.paginationGetAllPowerBills(powerBillFilter, DateUtil.getLDateFromString(date), skip, take);
    }

    @GetMapping("/send-notification-for-powerBills")
    public ResponseEntity<MessageResponse> sendMailForPowerBills(@RequestParam String date) throws MessagingException {
        LocalDate localDate = DateUtil.getLDateFromString(date);
        List<PowerBillDetail> list = powerBillService.getAllDetailPowerBills(localDate);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String subject = "Notification about power bill monthly";
        list.stream()
                .filter(powerBillDetail -> powerBillDetail.getBillId() != null)
                .collect(Collectors.toList())
                .forEach(powerBillDetail -> {
                    String contentMessageForPowerBills =
                            "Hi " + powerBillDetail.getDetailRoomDto().getName() + "\n"
                                    + "Please pay power bill for month: " + DateUtil.getLDateFromSDate(powerBillDetail.getEndDate()).getMonth() + "\n"
                                    + "Total money need to pay: " + powerBillDetail.getNumberOfMoneyMustPay();
                    powerBillDetail.getDetailRoomDto().getStudents().forEach(studentDto -> {
                        powerBillService.sendMail(contentMessageForPowerBills, subject, studentDto, javaMailSender, message, helper);
                    });
                });
        MessageResponse msg = new MessageResponse(
                HttpStatus.OK.value(),
                "SUCCESS",
                LocalDateTime.now());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public PowerBillDetail getADetailPowerBill(@RequestParam String date, @PathVariable Integer roomId) {
        return powerBillService.getAPowerBill(DateUtil.getLDateFromString(date), roomId);
    }

    @PutMapping("/{roomId}")
    public PowerBillDetail updatePowerBill(@PathVariable Integer roomId, @RequestBody PowerBillDetail powerBillDetail) {
        return powerBillService.updatePowerBill(roomId, powerBillDetail);
    }

    @PostMapping("/calculate-powerBill")
    public float calculatePowerBill(@RequestBody PowerBillDto powerBillDto) {
        return powerBillService.calculatePowerBill(powerBillDto);
    }

    @PostMapping("/send-notification")
    public ResponseEntity<MessageResponse> sendAttachmentEmail(@RequestBody PowerBillDetail powerBillDetail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String contentMessage = "Please pay power bill for month: " + DateUtil.getLDateFromSDate(powerBillDetail.getEndDate()).getMonth() + "\n"
                + "Total money need to pay: " + powerBillDetail.getNumberOfMoneyMustPay();
        String subject = "Notification about power bill monthly";
        powerBillDetail.getDetailRoomDto().getStudents().forEach(studentDto -> {
            powerBillService.sendMail(contentMessage, subject, studentDto, javaMailSender, message, helper);
        });
        MessageResponse msg = new MessageResponse(
                HttpStatus.OK.value(),
                "SUCCESS",
                LocalDateTime.now());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
