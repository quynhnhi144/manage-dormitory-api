package com.managedormitory.controllers;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.Mail;
import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dto.MessageResponse;
import com.managedormitory.models.dto.pagination.PaginationMail;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.services.MailService;
import com.managedormitory.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/mails")
public class MailController {
    @Autowired
    private MailService mailService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public PaginationMail getAllMails(@RequestParam(required = false) String subject, @RequestParam int skip, @RequestParam int take) {
        return mailService.filterAllMails(subject, skip, take);
    }

    @GetMapping("/{id}")
    public Mail getMailById(@PathVariable Integer id) {
        return mailService.getMailById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping("/addMail")
    public Mail addMail(@RequestBody Mail mail) {
        return mailService.addMail(mail);
    }

    @PutMapping("/{mailId}")
    public Mail updateMail(@PathVariable Integer mailId, @RequestBody Mail mail) {
        return mailService.updateMail(mailId, mail);
    }

    @DeleteMapping("/{mailId}")
    public void deleteMail(@PathVariable Integer mailId) {
        mailService.deleteMail(mailId);
    }

    @GetMapping("/sendMail/{mailId}")
    public ResponseEntity<MessageResponse> sendMail(@PathVariable Integer mailId) throws MessagingException {
        Mail mail = mailService.getMailById(mailId).orElseThrow(NotFoundException::new);
        List<StudentDetailDto> studentDetailDtos = studentService.getAllStayingStudent();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        studentDetailDtos.forEach(
                studentDetailDto -> {
                    mailService.sendMail(mail, studentDetailDto, javaMailSender, message, helper);
                }
        );
        MessageResponse msg = new MessageResponse(HttpStatus.OK.value(), "SUCCESS", LocalDateTime.now());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
