package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.Mail;
import com.managedormitory.models.dto.pagination.PaginationRegisterRoom;
import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.registerRoom.RegisterRoomIncludePayment;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.room.InfoMoneyDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.RegisterRoomFilter;
import com.managedormitory.repositories.custom.RegisterRoomRepositoryCustom;
import com.managedormitory.services.RegisterRoomService;
import com.managedormitory.services.RoomService;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegisterRoomServiceImpl implements RegisterRoomService {
    @Autowired
    private RegisterRoomRepositoryCustom registerRoomRepositoryCustom;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RoomService roomService;

    @Override
    public List<RegisterRoomDto> getAllRegisterRoom() {
        return registerRoomRepositoryCustom.getAllRegisterRoom();
    }

    @Override
    public PaginationRegisterRoom paginationGetAllRegisterRoom(RegisterRoomFilter registerRoomFilter, int skip, int take) {
        List<RegisterRoomDto> registerRoomDtos = getAllRegisterRoom();
        if (registerRoomFilter.getCampusName() != null) {
            registerRoomDtos = registerRoomDtos.stream()
                    .filter(registerRoomDto -> registerRoomDto.getCampusName().toLowerCase().equals(registerRoomFilter.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (registerRoomFilter.getRoomNameOrStudentNameOrIdCard() != null && !registerRoomFilter.getRoomNameOrStudentNameOrIdCard().equals("")) {
            String searchText = registerRoomFilter.getRoomNameOrStudentNameOrIdCard().toLowerCase() + StringUtil.DOT_STAR;
            registerRoomDtos = registerRoomDtos.stream()
                    .filter(registerRoomDto -> registerRoomDto.getRoomName().toLowerCase().matches(searchText)
                            || registerRoomDto.getStudentName().toLowerCase().matches(searchText)
                            || registerRoomDto.getIdCard().matches(searchText))
                    .collect(Collectors.toList());

        }

        int total = registerRoomDtos.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<RegisterRoomDto>> registerRoomMap = new LinkedHashMap<>();
        registerRoomMap.put("data", registerRoomDtos.subList(skip, lastElement));
        return new PaginationRegisterRoom(registerRoomMap, total);
    }

    @Override
    public RegisterRoomIncludePayment getRegisterRoom(Integer id) {
        RegisterRoomDto registerRoomDto = getAllRegisterRoom().stream()
                .filter(t -> t.getId() == id)
                .findFirst().orElse(null);
        InfoMoneyDto infoMoneyDto = studentService.getInfoMoneyDtoForNewStudent(registerRoomDto.getRoomId());
        return new RegisterRoomIncludePayment(registerRoomDto, infoMoneyDto);
    }

    @Override
    public RegisterRoomDto getRegisterRoomById(Integer id) {
        return getAllRegisterRoom().stream()
                .filter(t -> t.getId() == id)
                .findFirst().orElseThrow(() -> new NotFoundException("Không thể tìm thấy id: "));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DetailRoomDto addRegisterRoom(RegisterRoomDto registerRoomDto) {
        List<String> idCardListStayingStudent = studentService.getAllStayingStudent().stream()
                .map(StudentDetailDto::getIdCard).collect(Collectors.toList());

        List<String> idCardListRegisterRoom = getAllRegisterRoom().stream()
                .map(RegisterRoomDto::getIdCard).collect(Collectors.toList());
        if (!idCardListStayingStudent.contains(registerRoomDto.getIdCard()) && !idCardListRegisterRoom.contains(registerRoomDto.getIdCard())) {
            if (registerRoomRepositoryCustom.addRegisterRoom(registerRoomDto) > 0) {
              return roomService.getRoomById(registerRoomDto.getRoomId());
            } else {
                throw new BadRequestException("Không thể thưc hiện việc này!!!");
            }
        } else {
            throw new BadRequestException("Mã số sinh viên này đã bị trùng");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteRegisterRoom(Integer id) {
        if (registerRoomRepositoryCustom.deleteRegisterRoom(id) > 0) {
            return 1;
        } else {
            throw new BadRequestException("Khong the xoa duoc!!!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addStudentFromRegisterRoom(Integer registerRoomId, StudentNewDto studentNewDto) {
        StudentDto studentDto = studentService.addStudent(studentNewDto);
        int resultDeleteRegisterRoom = registerRoomRepositoryCustom.deleteRegisterRoom(registerRoomId);
        if (studentDto != null && resultDeleteRegisterRoom > 0) {
            return 1;
        } else {
            throw new BadRequestException("Khong them sinh vien vao phong duoc!!!");
        }
    }

    @Override
    public void sendMail(Mail mail, RegisterRoomDto registerRoomDto, JavaMailSender javaMailSender, SimpleMailMessage message) {
        System.out.println("registerRoomDto: " + registerRoomDto);
        message.setTo(registerRoomDto.getEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        javaMailSender.send(message);
    }

    @Override
    public void sendMail(Mail mail, StudentNewDto studentNewDto, JavaMailSender javaMailSender, SimpleMailMessage message) {
        message.setTo(studentNewDto.getStudentDto().getEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        javaMailSender.send(message);
    }


}
