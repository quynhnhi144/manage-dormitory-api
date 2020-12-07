package com.managedormitory.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.helper.ExportPDFBill;
import com.managedormitory.helper.PowerBillExcelHelper;
import com.managedormitory.models.dao.*;
import com.managedormitory.models.dto.*;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.room.*;
import com.managedormitory.models.dto.student.*;
import com.managedormitory.models.dto.vehicle.VehicleBillDto;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.repositories.PriceListRepository;
import com.managedormitory.repositories.StudentLeftRepository;
import com.managedormitory.repositories.StudentRepository;
import com.managedormitory.repositories.custom.*;
import com.managedormitory.services.StudentService;
import com.managedormitory.services.VehicleService;
import com.managedormitory.utils.CalculateMoney;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service


public class StudentServiceImpl implements StudentService {
    private static final LocalDate currentDate = LocalDate.now();

    private final StudentRepository studentRepository;

    private final StudentRepositoryCustom studentRepositoryCustom;

    private final StudentLeftRepository studentLeftRepository;

    private final RoomRepositoryCustom roomRepositoryCustom;

    private final StudentConvertToStudentDto studentConvertToStudentDto;

    private final WaterBillRepositoryCustom waterBillRepositoryCustom;

    private final DetailRoomRepositoryCustom detailRoomRepositoryCustom;

    private final SwitchRoomRepositoryCustom switchRoomRepositoryCustom;

    private final VehicleBillRepositoryCustom vehicleBillRepositoryCustom;

    private final PriceListRepository priceListRepository;

    private final VehicleRepositoryCustom vehicleRepositoryCustom;

    private final VehicleService vehicleService;

    public StudentServiceImpl(StudentRepository studentRepository, StudentRepositoryCustom studentRepositoryCustom, StudentLeftRepository studentLeftRepository, RoomRepositoryCustom roomRepositoryCustom, StudentConvertToStudentDto studentConvertToStudentDto, WaterBillRepositoryCustom waterBillRepositoryCustom, DetailRoomRepositoryCustom detailRoomRepositoryCustom, SwitchRoomRepositoryCustom switchRoomRepositoryCustom, VehicleBillRepositoryCustom vehicleBillRepositoryCustom, PriceListRepository priceListRepository, VehicleRepositoryCustom vehicleRepositoryCustom, VehicleService vehicleService) {
        this.studentRepository = studentRepository;
        this.studentRepositoryCustom = studentRepositoryCustom;
        this.studentLeftRepository = studentLeftRepository;
        this.roomRepositoryCustom = roomRepositoryCustom;
        this.studentConvertToStudentDto = studentConvertToStudentDto;
        this.waterBillRepositoryCustom = waterBillRepositoryCustom;
        this.detailRoomRepositoryCustom = detailRoomRepositoryCustom;
        this.switchRoomRepositoryCustom = switchRoomRepositoryCustom;
        this.vehicleBillRepositoryCustom = vehicleBillRepositoryCustom;
        this.priceListRepository = priceListRepository;
        this.vehicleRepositoryCustom = vehicleRepositoryCustom;
        this.vehicleService = vehicleService;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<StudentDetailDto> getAllStudentDto() {
        List<Student> students = getAllStudents();
        List<StudentLeft> studentLefts = getAllStudentLeft();
        List<Integer> studentLeftIds = studentLefts.stream().mapToInt(StudentLeft::getId).boxed().collect(Collectors.toList());
        List<StudentDto> studentDtos = studentRepositoryCustom.getAllStudentByTime();
        List<StudentDetailDto> studentDetailDtosDetail = new ArrayList<>();
        List<Integer> studentDtosIdList = studentDtos.stream().mapToInt(StudentDto::getId).boxed().collect(Collectors.toList());
        for (Student student : students) {
            StudentDetailDto studentDetailDto = new StudentDetailDto();
            studentDetailDto.setId(student.getId());
            studentDetailDto.setIdCard(student.getIdCard());
            studentDetailDto.setName(student.getName());
            studentDetailDto.setBirthday(student.getBirthday());
            studentDetailDto.setPhone(student.getPhone());
            studentDetailDto.setEmail(student.getEmail());
            studentDetailDto.setAddress(student.getAddress());
            studentDetailDto.setStartingDateOfStay(DateUtil.getSDateFromLDate(student.getStartingDateOfStay()));
            studentDetailDto.setWaterPriceId(student.getPriceList().getId());
            if (student.getVehicle() == null) {
                studentDetailDto.setVehicleId(null);
            } else {
                studentDetailDto.setVehicleId(student.getVehicle().getId());
            }
            if (student.getRoom() == null) {
                studentDetailDto.setRoomDto(null);
            } else {
                studentDetailDto.setRoomDto(new RoomDto(student.getRoom()));
            }

            if (studentDtosIdList.contains(student.getId())) {
                studentDetailDto.setIsPayRoom(true);
                studentDetailDto.setIsPayWaterBill(true);
            } else {
                studentDetailDto.setIsPayRoom(false);
                studentDetailDto.setIsPayWaterBill(false);
            }

            if (studentLeftIds.contains(student.getId())) {
                studentDetailDto.setActive(false);
            } else {
                studentDetailDto.setActive(true);
            }
            studentDetailDtosDetail.add(studentDetailDto);
        }
        return studentDetailDtosDetail;
    }

    @Override
    public List<StudentDetailDto> getAllStudentDtoActive() {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        List<StudentDetailDto> studentDetailDtosDetail = new ArrayList<>();
        List<String> studentLeftIdCards = getAllStudentLeft().stream().map(StudentLeft::getIdCard).collect(Collectors.toList());
        studentDetailDtos = studentDetailDtos.stream()
                .filter(studentDetailDto -> studentDetailDto.getVehicleId() == null)
                .collect(Collectors.toList());
        for (StudentDetailDto studentDetailDto : studentDetailDtos) {
            if (!studentLeftIdCards.contains(studentDetailDto.getIdCard())) {
                studentDetailDtosDetail.add(studentDetailDto);
            }
        }
        return studentDetailDtosDetail;
    }

    @Override
    public PaginationStudent paginationGetAllStudents(StudentFilterDto studentFilterDto, int skip, int take) {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        if (studentFilterDto.getCampusName() != null) {
            studentDetailDtos = studentDetailDtos.stream()
                    .filter(studentDto -> studentDto.getRoomDto() != null && studentDto.getRoomDto().getCampusName().toLowerCase().equals(studentFilterDto.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (studentFilterDto.getStudentNameOrRoomNameOrUserManager() != null && !studentFilterDto.getStudentNameOrRoomNameOrUserManager().equals("")) {
            String searchText = studentFilterDto.getStudentNameOrRoomNameOrUserManager().toLowerCase() + StringUtil.DOT_STAR;
            studentDetailDtos = studentDetailDtos.stream()
                    .filter(studentDto -> (studentDto.getRoomDto() != null && studentDto.getRoomDto().getName().toLowerCase().matches(searchText))
                            || (studentDto.getRoomDto() != null && studentDto.getRoomDto().getUserManager() != null && studentDto.getRoomDto().getUserManager().toLowerCase().matches(searchText))
                            || studentDto.getName().toLowerCase().matches(searchText)
                            || studentDto.getIdCard().matches(searchText))
                    .collect(Collectors.toList());
        }

        int total = studentDetailDtos.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<StudentDetailDto>> studentDtoMap = new HashMap<>();
        studentDtoMap.put("data", studentDetailDtos.subList(skip, lastElement));
        return new PaginationStudent(studentDtoMap, total);
    }

    @Override
    public StudentDetailDto getStudentById(Integer id) {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        List<StudentDetailDto> studentDetailDtoById = studentDetailDtos.stream()
                .filter(studentDto -> studentDto.getId().equals(id))
                .collect(Collectors.toList());
        if (studentDetailDtoById.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return studentDetailDtoById.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentDetailDto updateStudent(Integer id, StudentDto studentDto) throws BadRequestException {
        if (studentRepositoryCustom.updateStudent(id, studentDto) <= 0) {
            throw new BadRequestException("Cannot execute");
        }
        return getStudentById(id);
    }

    @Override
    public int countStudent() {
        return getAllStudentDto().size();
    }

    @Override
    public List<StudentLeft> getAllStudentLeft() {
        return studentLeftRepository.findAll();
    }

    @Override
    public RoomBillDto getDetailRoomRecently(Integer id) {
        return roomRepositoryCustom.getDetailRoomRecently(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentLeftDto getInfoMovingStudent(Integer id) {
        StudentDetailDto studentDetailDto = getStudentById(id);
        RoomBillDto roomBillDto = roomRepositoryCustom.getDetailRoomRecently(id).orElse(null);
        WaterBillDto waterBillDto = studentRepositoryCustom.getWaterBillRecently(id).orElse(null);

        VehicleBillDto vehicleBillDto = vehicleBillRepositoryCustom.getVehicleBillRecentlyByStudentId(id).orElse(null);
        float remainingMoneyOfVehicle = 0;
        if (vehicleBillDto != null) {
            remainingMoneyOfVehicle = CalculateMoney.calculateRemainingMoney(currentDate, DateUtil.getLDateFromSDate(vehicleBillDto.getEndDate()), vehicleBillDto.getPrice());
        }
        float remainingMoneyOfRoom = CalculateMoney.calculateRemainingMoney(currentDate, DateUtil.getLDateFromSDate(roomBillDto.getEndDate()), roomBillDto.getPrice() / roomBillDto.getMaxQuantity());
        float remainingMoneyOfWater = CalculateMoney.calculateRemainingMoney(currentDate, DateUtil.getLDateFromSDate(waterBillDto.getEndDate()), waterBillDto.getPrice());

        return new StudentLeftDto(id, studentDetailDto.getIdCard(), studentDetailDto.getName(), currentDate, remainingMoneyOfRoom, remainingMoneyOfWater, remainingMoneyOfVehicle, roomBillDto.getRoomId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentBill getInfoMoneyRoomPaymentDto(Integer studentId) {
        StudentDetailDto studentDetailDto = getStudentById(studentId);
        RoomBillDto roomBillDto = roomRepositoryCustom.getDetailRoomRecently(studentId).orElse(null);
        WaterBillDto waterBillDto = studentRepositoryCustom.getWaterBillRecently(studentId).orElse(null);
        VehicleBillDto vehicleBillDto = vehicleBillRepositoryCustom.getVehicleBillRecentlyByStudentId(studentId).orElse(null);
        LocalDate startDateRoom = DateUtil.getLDateFromSDate(roomBillDto.getEndDate());
        LocalDate endDateRoom = startDateRoom.plus(30, ChronoUnit.DAYS);
        LocalDate startDateWater = DateUtil.getLDateFromSDate(waterBillDto.getEndDate());
        LocalDate endDateWater = startDateWater.plus(30, ChronoUnit.DAYS);
        LocalDate startDateVehicle;
        LocalDate endDateVehicle;

        float remainingMoneyOfVehicle = 0;
        if (vehicleBillDto != null) {
            startDateVehicle = DateUtil.getLDateFromSDate(vehicleBillDto.getEndDate());
            endDateVehicle = startDateVehicle.plus(30, ChronoUnit.DAYS);
            remainingMoneyOfVehicle = CalculateMoney.calculateRemainingMoney(endDateVehicle, startDateVehicle, vehicleBillDto.getPrice());
        } else {
            startDateVehicle = null;
            endDateVehicle = null;
        }
        float remainingMoneyOfRoom = CalculateMoney.calculateRemainingMoney(endDateRoom, startDateRoom, roomBillDto.getPrice() / roomBillDto.getMaxQuantity());
        float remainingMoneyOfWater = CalculateMoney.calculateRemainingMoney(endDateWater, startDateWater, waterBillDto.getPrice());
        return new StudentBill(studentId, studentDetailDto.getName(), studentDetailDto.getRoomDto().getId(), studentDetailDto.getRoomDto().getName(), studentDetailDto.getIdCard(), startDateRoom, endDateRoom, remainingMoneyOfRoom, studentDetailDto.getRoomDto().getQuantityStudent(), startDateWater, endDateWater, remainingMoneyOfWater, studentDetailDto.getVehicleId(), startDateVehicle, endDateVehicle, remainingMoneyOfVehicle);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addStudentLeft(StudentLeftDto studentMoveDto) {
        int resultStudentLeft = studentRepositoryCustom.addStudentLeft(studentMoveDto);
        int resultVehicleLeft = 0;
        if (vehicleService.getVehicleByStudentId(studentMoveDto.getId()) != null) {
            resultVehicleLeft = vehicleRepositoryCustom.addVehicleLeft(vehicleService.getVehicleByStudentId(studentMoveDto.getId()));
        }
        int resultRoom = roomRepositoryCustom.updateQuantityStudent(studentMoveDto.getRoomId());
        if (vehicleService.getVehicleByStudentId(studentMoveDto.getId()) != null) {
            if (resultStudentLeft > 0 && resultRoom > 0 && resultVehicleLeft > 0) {
                return 1;
            } else {
                throw new BadRequestException("Cannot implement method!!!");
            }
        } else {
            if (resultStudentLeft > 0 && resultRoom > 0) {
                return 1;
            } else {
                throw new BadRequestException("Cannot implement method!!!");
            }
        }

    }

    @Override
    public InfoMoneyDto getInfoMoneyDtoForNewStudent(Integer roomId) {
        InfoMoney infoMoney = roomRepositoryCustom.getInfoLatestBillForNewStudent(roomId).orElse(null);

        PriceList priceListWater = priceListRepository.findById(2).orElse(null);
        PriceList priceListVehicle = priceListRepository.findById(3).orElse(null);
        LocalDate currentDateRoom = currentDate;
        LocalDate endDateRoom;

        LocalDate currentDateWater = currentDate;
        LocalDate endDateWater;

        LocalDate currentDateVehicle = currentDate;
        LocalDate endDateVehicle = LocalDate.now();
        float remainingMoneyOfRoom;
        float remainingMoneyOfWater;
        float remainingMoneyOfVehicle;
        if (infoMoney == null) {
            endDateRoom = currentDate.plus(30, ChronoUnit.DAYS);
            endDateWater = currentDate.plus(30, ChronoUnit.DAYS);
            endDateVehicle = currentDate.plus(30, ChronoUnit.DAYS);
        } else {
            if (infoMoney.getWaterPrice() == null) {
                infoMoney.setWaterPrice(priceListWater.getPrice());
            }
            if (infoMoney.getMaxDateRoomBill() != null) {
                if (DateUtil.getLDateFromSDate(infoMoney.getMaxDateRoomBill()).isBefore(currentDate)) {
                    endDateRoom = DateUtil.getLDateFromSDate(infoMoney.getMaxDateRoomBill()).plus(30, ChronoUnit.DAYS);
                    endDateWater = DateUtil.getLDateFromSDate(infoMoney.getMaxDateWaterBill()).plus(30, ChronoUnit.DAYS);
                } else {
                    endDateRoom = DateUtil.getLDateFromSDate(infoMoney.getMaxDateRoomBill());
                    endDateWater = DateUtil.getLDateFromSDate(infoMoney.getMaxDateWaterBill());
                }
            } else {
                endDateRoom = currentDate.plus(30, ChronoUnit.DAYS);
                endDateWater = currentDate.plus(30, ChronoUnit.DAYS);
            }

            if (infoMoney.getMaxDateVehicleBill() == null) {
                currentDateVehicle = currentDate;
                endDateVehicle = endDateRoom;
            } else {
                if (DateUtil.getLDateFromSDate(infoMoney.getMaxDateVehicleBill()).isBefore(currentDate)) {
                    endDateVehicle = DateUtil.getLDateFromSDate(infoMoney.getMaxDateVehicleBill()).plus(30, ChronoUnit.DAYS);
                } else {
                    endDateVehicle = DateUtil.getLDateFromSDate(infoMoney.getMaxDateVehicleBill());
                }
            }
        }

        remainingMoneyOfRoom = CalculateMoney.calculateRemainingMoney(currentDateRoom, endDateRoom, infoMoney.getRoomPrice() / infoMoney.getMaxQuantityStudent());
        remainingMoneyOfWater = CalculateMoney.calculateRemainingMoney(currentDateWater, endDateWater, infoMoney.getWaterPrice());
        remainingMoneyOfVehicle = CalculateMoney.calculateRemainingMoney(currentDateVehicle, endDateVehicle, priceListVehicle.getPrice());

        return new InfoMoneyDto(roomId, infoMoney.getRoomName(), currentDateRoom, endDateRoom, remainingMoneyOfRoom, infoMoney.getWaterPriceId(), currentDateWater, endDateWater, remainingMoneyOfWater, currentDateVehicle, endDateVehicle, remainingMoneyOfVehicle, infoMoney.getMaxQuantityStudent());
    }


    @Override
    public DurationBetweenTwoRoom durationMoneyBetweenTwoRoom(Integer oldRoomId, Integer newRoomId) {
        if (oldRoomId != null && newRoomId != null) {
            InfoMoneyDto oldRoom = getInfoMoneyDtoForNewStudent(oldRoomId);
            System.out.println("tien phong cu: " + oldRoom.getMoneyOfRoomMustPay());
            System.out.println("tien nuoc cu: " + oldRoom.getMoneyOfWaterMustPay());
            System.out.println("tien xe cu: " + oldRoom.getMoneyOfVehicleMustPay());
            System.out.println("oldRoom: " + oldRoom);
            InfoMoneyDto newRoom = getInfoMoneyDtoForNewStudent(newRoomId);
            System.out.println("========================");
            System.out.println("tien phong moi: " + newRoom.getMoneyOfRoomMustPay());
            System.out.println("tien nuoc moi: " + newRoom.getMoneyOfWaterMustPay());
            System.out.println("tien xe moi: " + newRoom.getMoneyOfVehicleMustPay());
            System.out.println("newRoom: " + newRoom);


            float durationRoomMoney = oldRoom.getMoneyOfRoomMustPay() - newRoom.getMoneyOfRoomMustPay();
            float durationWaterMoney = oldRoom.getMoneyOfWaterMustPay() - newRoom.getMoneyOfWaterMustPay();
            float durationVehicleMoney = oldRoom.getMoneyOfVehicleMustPay() - newRoom.getMoneyOfVehicleMustPay();

            return new DurationBetweenTwoRoom(oldRoomId, newRoomId, oldRoom.getRoomName(), newRoom.getRoomName(), currentDate, newRoom.getRoomEndDate(), durationRoomMoney, currentDate, newRoom.getWaterEndDate(), durationWaterMoney, currentDate, newRoom.getVehicleEndDate(), durationVehicleMoney);
        }
        throw new BadRequestException("Không được để trống id của phòng mới!!!!");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentDto addStudent(StudentNewDto studentNewDto) {
        int resultAddStudent = studentRepositoryCustom.addStudent(studentNewDto.getStudentDto());

        if (resultAddStudent > 0) {
            StudentDto studentDto = studentConvertToStudentDto.convert(getAllStudents().get(getAllStudents().size() - 1));
            RoomBillDto roomBillDto = studentNewDto.getRoomBillDto();
            roomBillDto.setStudentId(studentDto.getId());
            roomBillDto.setStudentName(studentDto.getName());
            roomBillDto.setRoomId(studentDto.getRoomId());

            WaterBillDto waterBillDto = studentNewDto.getWaterBillDto();
            waterBillDto.setStudentId(studentDto.getId());
            waterBillDto.setStudentName(studentDto.getName());
            waterBillDto.setRoomId(studentDto.getRoomId());
            int resultUpdateQuantityStudent = roomRepositoryCustom.updateQuantityStudent(studentDto.getRoomId());
            int resultDetailRoom = detailRoomRepositoryCustom.addDetailRoom(roomBillDto);
            int resultWaterBill = waterBillRepositoryCustom.addWaterBill(waterBillDto);

            if (resultUpdateQuantityStudent > 0 && resultDetailRoom > 0 && resultWaterBill > 0) {
                return studentDto;
            } else {
                throw new BadRequestException("Không thể thực hiện hành động này!!!");
            }
        }
        return null;
    }

    @Override
    public ByteArrayInputStream exportPDFStudentNew(StudentNewDto studentNewDto) {
        ExportPDFBill exportPDFBill = new ExportPDFBill(studentNewDto);
        Document document
                = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            exportPDFBill.writeBillHeader(document);
            exportPDFBill.writeBillData(t -> {
                BaseFont baseFont = null;
                try {
                    baseFont = BaseFont.createFont(exportPDFBill.getFontName(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font font = new Font(baseFont, 16);

                    Paragraph infoStudent = new Paragraph();
                    infoStudent.add(new Paragraph("Mã số sinh viên:        " + studentNewDto.getStudentDto().getIdCard(), font));

                    infoStudent.add(new Paragraph("Họ và tên sinh viên:   " + studentNewDto.getStudentDto().getName(), font));

                    infoStudent.add(new Paragraph("Số tiền là:                " + Math.abs(studentNewDto.getRoomBillDto().getPrice()) + " đ" + " (Tiền phòng) - " + Math.abs(studentNewDto.getWaterBillDto().getPrice()) + " đ" + " (Tiền nước)", font));
                    infoStudent.add(new Paragraph("\n", font));
                    document.add(infoStudent);
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }

            });
            exportPDFBill.writeBillFooter(document);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportPDFStudentRemove(StudentLeftDto studentMoveDto) {
        ExportPDFBill exportPDFBill = new ExportPDFBill(studentMoveDto);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            exportPDFBill.writeBillHeader(document);
            exportPDFBill.writeBillData(t -> {
                BaseFont baseFont = null;
                try {
                    baseFont = BaseFont.createFont(exportPDFBill.getFontName(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font font = new Font(baseFont, 16);

                    Paragraph infoStudent = new Paragraph();
                    infoStudent.add(new Paragraph("Mã số sinh viên:                        " + studentMoveDto.getIdCard(), font));
                    infoStudent.add(new Paragraph("Họ và tên sinh viên:                   " + studentMoveDto.getName(), font));
                    String moneyPayOrTake = "Số tiền " + (studentMoveDto.getNumberOfRoomMoney() > 0 ? "phải trả là: " : "được nhận là: ");
                    infoStudent.add(new Paragraph(moneyPayOrTake + "                " + Math.abs(studentMoveDto.getNumberOfRoomMoney()) + " đ" + " (Tiền phòng) - " + Math.abs(studentMoveDto.getNumberOfWaterMoney()) + " đ" + " (Tiền nước) - " + Math.abs(studentMoveDto.getNumberOfVehicleMoney()) + " đ" + " (Tiền xe)", font));
                    infoStudent.add(new Paragraph("\n", font));
                    document.add(infoStudent);
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }

            });
            exportPDFBill.writeBillFooter(document);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportPDFStudentSwitchRoom(InfoSwitchRoom studentSwitchRoom) {
        ExportPDFBill exportPDFBill = new ExportPDFBill(studentSwitchRoom);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            exportPDFBill.writeBillHeader(document);
            exportPDFBill.writeBillData(t -> {
                BaseFont baseFont = null;
                try {
                    baseFont = BaseFont.createFont(exportPDFBill.getFontName(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font font = new Font(baseFont, 16);

                    Paragraph infoStudent = new Paragraph();
                    infoStudent.add(new Paragraph("Mã số sinh viên:                        " + studentSwitchRoom.getStudentIdCard(), font));
                    infoStudent.add(new Paragraph("Họ và tên sinh viên:                   " + studentSwitchRoom.getStudentName(), font));
                    String moneyPayOrTake = "Số tiền " + (studentSwitchRoom.getRoomBill().getPrice() > 0 ? "phải trả là: " : "được nhận là: ");
                    infoStudent.add(new Paragraph(moneyPayOrTake + "                " + Math.abs(studentSwitchRoom.getRoomBill().getPrice()) + " đ" + " (Tiền phòng) - " + Math.abs(studentSwitchRoom.getWaterBill().getPrice()) + " đ" + " (Tiền nước) - " + Math.abs(studentSwitchRoom.getVehicleBill().getPrice()) + " đ" + " (Tiền xe)", font));
                    infoStudent.add(new Paragraph("\n", font));
                    document.add(infoStudent);
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }

            });
            exportPDFBill.writeBillFooter(document);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportPDFStudentPayment(StudentBill studentBill) {
        ExportPDFBill exportPDFBill = new ExportPDFBill(studentBill);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            exportPDFBill.writeBillHeader(document);
            exportPDFBill.writeBillData(t -> {
                BaseFont baseFont = null;
                try {
                    baseFont = BaseFont.createFont(exportPDFBill.getFontName(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font font = new Font(baseFont, 16);

                    Paragraph infoStudent = new Paragraph();
                    infoStudent.add(new Paragraph("Mã số sinh viên:                        " + studentBill.getStudentIdCard(), font));
                    infoStudent.add(new Paragraph("Họ và tên sinh viên:                   " + studentBill.getStudentName(), font));
                    String moneyPayOrTake = "Số tiền " + (studentBill.getMoneyOfRoomMustPay() > 0 ? "phải trả là: " : "được nhận là: ");
                    infoStudent.add(new Paragraph(moneyPayOrTake + "                " + Math.abs(studentBill.getMoneyOfRoomMustPay()) + " đ" + " (Tiền phòng) - " + Math.abs(studentBill.getMoneyOfWaterMustPay()) + " đ" + " (Tiền nước) - " + Math.abs(studentBill.getMoneyOfVehicleMustPay()) + " đ" + " (Tiền xe)", font));
                    infoStudent.add(new Paragraph("\n", font));
                    document.add(infoStudent);
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }

            });
            exportPDFBill.writeBillFooter(document);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportExcel() throws IOException {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        PowerBillExcelHelper<StudentDetailDto> powerBillExcelHelper = new PowerBillExcelHelper(studentDetailDtos);
        powerBillExcelHelper.writeHeaderLine(StringUtil.HEADER_STUDENTS, StringUtil.SHEET_STUDENT);
        powerBillExcelHelper.writeDataLines(studentDetailDto -> {
            int rowCount = 1;
            CellStyle style = powerBillExcelHelper.getWorkbook().createCellStyle();
            XSSFFont font = powerBillExcelHelper.getWorkbook().createFont();
            font.setFontHeight(14);
            style.setFont(font);

            for (StudentDetailDto studentDetailDtoCurrent : studentDetailDtos) {
                Row row = powerBillExcelHelper.getSheet().createRow(rowCount++);
                int columnCount = 0;
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getId(), style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getIdCard(), style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getName(), style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getPhone(), style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getAddress(), style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getRoomDto().getName(), style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getIsPayRoom() ? "x" : "--", style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.getIsPayWaterBill() ? "x" : "--", style);
                powerBillExcelHelper.createCell(row, columnCount++, studentDetailDtoCurrent.isActive() ? "x" : "--", style);
            }
        });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        powerBillExcelHelper.getWorkbook().write(outputStream);
        powerBillExcelHelper.getWorkbook().close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int switchRoomForStudent(InfoSwitchRoom infoSwitchRoom, Integer studentId) {
        System.out.println("Info switch room: " + infoSwitchRoom);
        Integer oldRoomId = infoSwitchRoom.getOldRoomId();
        Integer newRoomId = infoSwitchRoom.getNewRoomId();
        int resultUpdateRoomIdForStudent = studentRepositoryCustom.updateRoomIdForStudent(studentId, newRoomId);
        int resultUpdateQuantityStudentOldRoom = roomRepositoryCustom.updateQuantityStudent(oldRoomId);
        int resultUpdateQuantityStudentNewRoom = roomRepositoryCustom.updateQuantityStudent(newRoomId);

        DurationBetweenTwoRoom durationBetweenTwoRoom = durationMoneyBetweenTwoRoom(oldRoomId, newRoomId);
        SwitchRoomHistoryDto switchRoomHistoryDto = null;

        int resultAddDetailRoom = detailRoomRepositoryCustom.addDetailRoom(infoSwitchRoom.getRoomBill());
        int resultAddWaterBill = waterBillRepositoryCustom.addWaterBill(infoSwitchRoom.getWaterBill());
        int resultAddVehicleBill = 0;
        if (infoSwitchRoom.getVehicleBill().getVehicleId() != null) {
            resultAddVehicleBill = vehicleBillRepositoryCustom.addVehicleBillRepository(infoSwitchRoom.getVehicleBill());
            switchRoomHistoryDto = new SwitchRoomHistoryDto(null, infoSwitchRoom.getStudentIdCard(), durationBetweenTwoRoom.getOldRoomName(), durationBetweenTwoRoom.getNewRoomName(), durationBetweenTwoRoom.getDurationRoomMoney(), durationBetweenTwoRoom.getDurationWaterMoney(), durationBetweenTwoRoom.getDurationVehicleMoney(), studentId, DateUtil.getSDateFromLDate(currentDate));
        } else {
            switchRoomHistoryDto = new SwitchRoomHistoryDto(null, infoSwitchRoom.getStudentIdCard(), durationBetweenTwoRoom.getOldRoomName(), durationBetweenTwoRoom.getNewRoomName(), durationBetweenTwoRoom.getDurationRoomMoney(), durationBetweenTwoRoom.getDurationWaterMoney(), 0, studentId, DateUtil.getSDateFromLDate(currentDate));
        }
        int resultAddSwitchRoomHistory = switchRoomRepositoryCustom.addSwitchRoomHistory(switchRoomHistoryDto);
        if (infoSwitchRoom.getVehicleBill().getVehicleId() == null) {
            if (resultUpdateRoomIdForStudent > 0 && resultUpdateQuantityStudentNewRoom > 0
                    && resultUpdateQuantityStudentOldRoom > 0 && resultAddDetailRoom > 0 && resultAddWaterBill > 0 && resultAddSwitchRoomHistory > 0) {
                return 1;
            } else {
                throw new BadRequestException("Khong the thuc hien chuyen phong cho sinh vien " + studentId);
            }
        } else {
            if (resultUpdateRoomIdForStudent > 0 && resultUpdateQuantityStudentNewRoom > 0
                    && resultUpdateQuantityStudentOldRoom > 0 && resultAddDetailRoom > 0 && resultAddWaterBill > 0 && resultAddVehicleBill > 0 && resultAddSwitchRoomHistory > 0) {
                return 1;
            } else {
                throw new BadRequestException("Khong the thuc hien chuyen phong cho sinh vien " + studentId);
            }
        }
    }

    @Override
    public List<StudentDetailDto> getStudentsByIdCard(String idCard) {
        return getAllStudentDto().stream()
                .filter(studentDetailDto -> studentDetailDto.getName().equals(idCard))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int registerRemainingRoomForStudent(RegisterRoomDto registerRoomDto) {
        if (studentRepositoryCustom.registerRemainingRoomForStudent(registerRoomDto) > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addPaymentForStudent(StudentBill studentBill) {
        RoomBillDto roomBillDto = new RoomBillDto(studentBill);
        WaterBillDto waterBillDto = new WaterBillDto(studentBill);
        int resultVehicleBill = 0;
        int resultDetailRoom = detailRoomRepositoryCustom.addDetailRoom(roomBillDto);
        int resultWaterBill = waterBillRepositoryCustom.addWaterBill(waterBillDto);
        if (studentBill.getVehicleId() != null) {
            VehicleBillDto vehicleBillDto = new VehicleBillDto(studentBill);
            resultVehicleBill = vehicleBillRepositoryCustom.addVehicleBillRepository(vehicleBillDto);
            if (resultDetailRoom > 0 && resultWaterBill > 0 && resultVehicleBill > 0) {
                return studentBill.getStudentId();
            } else {
                throw new BadRequestException("Không thể thực hiện hành động này!!!");
            }
        } else {
            if (resultDetailRoom > 0 && resultWaterBill > 0) {
                return studentBill.getStudentId();
            } else {
                throw new BadRequestException("Không thể thực hiện hành động này!!!");
            }
        }
    }

}
