package com.managedormitory.controllers;

import com.managedormitory.models.dao.Mail;
import com.managedormitory.models.dto.MessageResponse;
import com.managedormitory.models.dto.pagination.PaginationRegisterRoom;
import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.registerRoom.RegisterRoomIncludePayment;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.RegisterRoomFilter;
import com.managedormitory.services.RegisterRoomService;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("api/registerRooms")
public class RegisterRoomController {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RegisterRoomService registerRoomService;

    private RegisterRoomDto registerRoomDto = new RegisterRoomDto();


    @GetMapping()
    public PaginationRegisterRoom filterRegisterRoom(@RequestParam(required = false) String campusName, @RequestParam int skip, @RequestParam int take, @RequestParam(required = false) String searchText) {
        RegisterRoomFilter registerRoomFilter = RegisterRoomFilter.builder().campusName(campusName).roomNameOrStudentNameOrIdCard(searchText).build();
        return registerRoomService.paginationGetAllRegisterRoom(registerRoomFilter, skip, take);
    }


    @GetMapping("/{id}")
    public RegisterRoomIncludePayment getRegisterRoomPayment(@PathVariable Integer id) {
        return registerRoomService.getRegisterRoom(id);
    }

    @GetMapping("/sendMail/notification-about-delete-register-room")
    public ResponseEntity<MessageResponse> sendMailDeleteRegisterRoom() {
        Mail mail = new Mail();
        mail.setSubject(StringUtil.SUBJECT_SUCCESS_REGISTER_ROOM);
        mail.setContent(StringUtil.CONTENT_REJECT_REGISTER_ROOM);
        SimpleMailMessage message = new SimpleMailMessage();
        registerRoomService.sendMail(mail, registerRoomDto, javaMailSender, message);
        MessageResponse msg = new MessageResponse(HttpStatus.OK.value(), "SUCCESS", LocalDateTime.now());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping()
    public int registerRemainingRoomForStudent(@RequestBody RegisterRoomDto registerRoomDto) {
        return studentService.registerRemainingRoomForStudent(registerRoomDto);
    }


    @PostMapping("/{id}/addStudent")
    public int addStudentFromRegisterRoom(@PathVariable Integer id, @RequestBody StudentNewDto studentNewDto) {
        return registerRoomService.addStudentFromRegisterRoom(id, studentNewDto);
    }

    @PostMapping("/sendMail/notification-about-register")
    public ResponseEntity<MessageResponse> sendMail(@RequestBody RegisterRoomDto registerRoomDto) {
        Mail mail = new Mail();
        mail.setSubject(StringUtil.SUBJECT_SUCCESS_REGISTER_ROOM);
        mail.setContent(StringUtil.CONTENT_SUCCESS_REGISTER_ROOM);
        SimpleMailMessage message = new SimpleMailMessage();
        registerRoomService.sendMail(mail, registerRoomDto, javaMailSender, message);
        MessageResponse msg = new MessageResponse(HttpStatus.OK.value(), "SUCCESS", LocalDateTime.now());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/sendMail/notification-about-successfully-register")
    public ResponseEntity<MessageResponse> sendMailSuccessFullyRegister(@RequestBody StudentNewDto studentNewDto) {
        Mail mail = new Mail();
        mail.setSubject(StringUtil.SUBJECT_SUCCESS_REGISTER_ROOM);
        mail.setContent(StringUtil.CONTENT_SUCCESS_ADD_STUDENT);
        SimpleMailMessage message = new SimpleMailMessage();
        registerRoomService.sendMail(mail, studentNewDto, javaMailSender, message);
        MessageResponse msg = new MessageResponse(HttpStatus.OK.value(), "SUCCESS", LocalDateTime.now());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public int deleteRegisterRoom(@PathVariable Integer id) {
        registerRoomDto = registerRoomService.getRegisterRoomById(id);
        return registerRoomService.deleteRegisterRoom(id);
    }

}
