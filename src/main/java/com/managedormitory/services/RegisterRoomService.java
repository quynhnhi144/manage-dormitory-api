package com.managedormitory.services;

import com.managedormitory.models.dao.Mail;
import com.managedormitory.models.dto.pagination.PaginationRegisterRoom;
import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.registerRoom.RegisterRoomIncludePayment;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.RegisterRoomFilter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.List;

public interface RegisterRoomService {
    List<RegisterRoomDto> getAllRegisterRoom();

    PaginationRegisterRoom paginationGetAllRegisterRoom(RegisterRoomFilter registerRoomFilter, int skip, int take);

    RegisterRoomIncludePayment getRegisterRoom(Integer id);

    RegisterRoomDto getRegisterRoomById(Integer id);

    DetailRoomDto addRegisterRoom(RegisterRoomDto registerRoomDto);

    int deleteRegisterRoom(Integer id);

    int addStudentFromRegisterRoom(Integer registerRoomId, StudentNewDto studentNewDto);

    void sendMail(Mail mail, RegisterRoomDto registerRoomDto, JavaMailSender javaMailSender, SimpleMailMessage message);

    void sendMail(Mail mail, StudentNewDto studentNewDto, JavaMailSender javaMailSender, SimpleMailMessage message);

}
