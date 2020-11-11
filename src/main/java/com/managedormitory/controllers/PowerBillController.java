package com.managedormitory.controllers;

import com.managedormitory.models.dto.MessageResponse;
import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.filter.PowerBillFilter;
import com.managedormitory.services.PowerBillService;
import com.managedormitory.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("api/powerBills")
public class PowerBillController {

    @Autowired
    private PowerBillService powerBillService;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping
    public PaginationPowerBill filterPowerBill(@RequestParam(required = false) String campusName, @RequestParam(required = false) String searchText, @RequestParam int skip, @RequestParam int take) {
        PowerBillFilter powerBillFilter = PowerBillFilter.builder().campusName(campusName).roomName(searchText).build();
        return powerBillService.paginationGetAllPowerBills(powerBillFilter, skip, take);
    }

    @GetMapping("/{roomId}")
    public PowerBillDetail getADetailPowerBill(@PathVariable Integer roomId) {
        return powerBillService.getAPowerBill(roomId);
    }

    @PostMapping("/send-notification")
    public ResponseEntity<MessageResponse> sendAttachmentEmail(@RequestBody PowerBillDetail powerBillDetail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);
        String contentMessage = "Please pay power bill for month: " + DateUtil.getLDateFromSDate(powerBillDetail.getEndDate()).getMonth() + "\n"
                + "Total money need to pay: " + powerBillDetail.getNumberOfMoneyMustPay();
        powerBillDetail.getDetailRoomDto().getStudents().forEach(studentDto -> {
            try {
                helper.setTo(studentDto.getEmail());
                helper.setSubject("Notification about power bill monthly");
                helper.setText(contentMessage);
                String path = "/home/nhile/Downloads/link.txt";

                // Attachment
                FileSystemResource file = new FileSystemResource(new File(path));
                helper.addAttachment("link", file);
                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

//        String contentMessage = "Please pay power bill for month: " + "\n"
//                + "Total money need to pay: " + powerBillDetail.getNumberOfMoneyMustPay();
//        SimpleMailMessage message = new SimpleMailMessage();

//        powerBillDetail.getDetailRoomDto().getStudents().forEach(studentDto -> {
//            message.setTo(studentDto.getEmail());
//            message.setSubject("Notification about power bill monthly");
//            message.setText(contentMessage);
//
//            javaMailSender.send(message);
//        });
        MessageResponse msg = new MessageResponse(
                HttpStatus.OK.value(),
                "SUCCESS",
                LocalDateTime.now());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
