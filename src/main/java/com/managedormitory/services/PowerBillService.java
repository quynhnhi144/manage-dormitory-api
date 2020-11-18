package com.managedormitory.services;

import com.managedormitory.models.dto.MessageResponse;
import com.managedormitory.models.dto.PowerBillImport;
import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.filter.PowerBillFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface PowerBillService {
    List<PowerBillDetail> getAllDetailPowerBills(LocalDate date);

    List<PowerBillDetail> getAllPowerBillByMaxEndDate();

    PaginationPowerBill paginationGetAllPowerBills(PowerBillFilter powerBillFilter, LocalDate date, int skip, int take);


    PowerBillDetail getAPowerBill(LocalDate date, Integer roomId);

    PowerBillDetail updatePowerBill(Integer roomId, PowerBillDetail powerBillDetail);

    ByteArrayInputStream exportExcelFile(LocalDate currentDate) throws IOException;

    void sendMail(String text, String subject, StudentDto studentDto, JavaMailSender javaMailSender, MimeMessage message, MimeMessageHelper mimeMessageHelper);

    int importExcelFile(MultipartFile multipartFile, LocalDate localDate);

    PowerBillDetail addPowerBill(Integer roomId, PowerBillDetail powerBillDetail);
}
