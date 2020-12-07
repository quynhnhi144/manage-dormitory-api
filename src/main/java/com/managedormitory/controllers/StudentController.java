package com.managedormitory.controllers;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dto.DurationBetweenTwoRoom;
import com.managedormitory.models.dto.InfoSwitchRoom;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.room.InfoMoneyDto;
import com.managedormitory.models.dto.student.*;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public PaginationStudent filterStudent(@RequestParam(required = false) String campusName, @RequestParam(required = false) String searchText, @RequestParam int skip, @RequestParam int take) {
        StudentFilterDto studentFilterDto = StudentFilterDto.builder().campusName(campusName).studentNameOrRoomNameOrUserManager(searchText).build();
        return studentService.paginationGetAllStudents(studentFilterDto, skip, take);
    }

    @GetMapping("/all")
    public List<StudentDetailDto> getAllStudents() {
        return studentService.getAllStudentDto();
    }

    @GetMapping("/allStudentActive")
    public List<StudentDetailDto> getAllStudentsActive() {
        return studentService.getAllStudentDtoActive();
    }

    @GetMapping("/{id}")
    public StudentDetailDto getDetailAStudent(@PathVariable Integer id) {
        try {
            return studentService.getStudentById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/{id}/studentLeft")
    public StudentLeftDto getRoomBillDto(@PathVariable Integer id) {
        try {
            return studentService.getInfoMovingStudent(id);
        } catch (Exception e) {
            throw new NotFoundException("Cannot find this " + id);
        }
    }

    @GetMapping("/{roomId}/money-room-and-money-water")
    public InfoMoneyDto getRoomPriceAndWaterPrice(@PathVariable Integer roomId) {
        return studentService.getInfoMoneyDtoForNewStudent(roomId);
    }

    @GetMapping("/duration_money_between_two_room")
    public DurationBetweenTwoRoom durationMoneyBetweenTwoRoom(@RequestParam Integer oldRoomId, @RequestParam Integer newRoomId) {
        return studentService.durationMoneyBetweenTwoRoom(oldRoomId, newRoomId);
    }

    @GetMapping("/student")
    public List<StudentDetailDto> getStudentByName(@RequestParam(required = false) String studentIdCard) {
        return studentService.getStudentsByIdCard(studentIdCard);
    }

    @GetMapping("/exportExcel")
    public ResponseEntity<Resource> exportExcelFile() {
        try {
            InputStreamResource file = new InputStreamResource(studentService.exportExcel());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + StringUtil.FILE_NAME_EXCEL_POWER_BILL)
                    .contentType(MediaType.parseMediaType(StringUtil.MEDIA_TYPE))
                    .body(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("/exportPDF")
    public ResponseEntity<Resource> exportToPDF(@RequestBody StudentNewDto studentNewDto) {
        InputStreamResource file = new InputStreamResource(studentService.exportPDFStudentNew(studentNewDto));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + studentNewDto.getStudentDto().getIdCard() + LocalDate.now().toString() + ".pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/exportPDFRemoveStudent")
    public ResponseEntity<Resource> exportPDFStudentRemove(@RequestBody StudentLeftDto studentMoveDto) {
        InputStreamResource file = new InputStreamResource(studentService.exportPDFStudentRemove(studentMoveDto));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + studentMoveDto.getIdCard() + LocalDate.now().toString() + ".pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/exportPDFSwitchRoomStudent")
    public ResponseEntity<Resource> exportPDFSwitchRoomStudent(@RequestBody InfoSwitchRoom infoSwitchRoom) {
        InputStreamResource file = new InputStreamResource(studentService.exportPDFStudentSwitchRoom(infoSwitchRoom));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + infoSwitchRoom.getStudentIdCard() + LocalDate.now().toString() + ".pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/exportPDFStudentPayment")
    public ResponseEntity<Resource> exportPDFStudentPayment(@RequestBody StudentBill studentBill) {
        InputStreamResource file = new InputStreamResource(studentService.exportPDFStudentPayment(studentBill));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + studentBill.getStudentIdCard() + LocalDate.now().toString() + ".pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/addStudent")
    public StudentDto addStudent(@RequestBody StudentNewDto studentNewDto) {
        return studentService.addStudent(studentNewDto);
    }

    @PostMapping("/studentLeft")
    public int addStudentLeft(@RequestBody StudentLeftDto studentMoveDto) {
        return studentService.addStudentLeft(studentMoveDto);
    }

    @PostMapping("/create-payment")
    public int createPayment(@RequestBody StudentBill studentBill) {
        return studentService.addPaymentForStudent(studentBill);
    }

    @PutMapping("/{id}")
    public StudentDetailDto updateStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) throws BadRequestException {
        return studentService.updateStudent(id, studentDto);
    }

    @PutMapping("/{id}/switch-room")
    public int switchRoomForStudent(@PathVariable Integer id, @RequestBody InfoSwitchRoom infoSwitchRoom) {
        return studentService.switchRoomForStudent(infoSwitchRoom, id);
    }
}
