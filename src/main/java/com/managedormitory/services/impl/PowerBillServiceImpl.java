package com.managedormitory.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.managedormitory.converters.StudentConvertToStudentDto;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.helper.ExportPDFBill;
import com.managedormitory.helper.PowerBillExcelHelper;
import com.managedormitory.helper.PowerBillReadExcelHelper;
import com.managedormitory.models.dao.PriceList;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.PowerBillImport;
import com.managedormitory.models.dto.pagination.PaginationPowerBill;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.PowerBillFilter;
import com.managedormitory.repositories.custom.PowerBillRepositoryCustom;
import com.managedormitory.services.PowerBillService;
import com.managedormitory.services.RoomService;
import com.managedormitory.utils.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PowerBillServiceImpl implements PowerBillService {
    @Autowired
    private PowerBillRepositoryCustom powerBillRepositoryCustom;

    @Autowired
    private RoomService roomService;

    @Autowired
    private StudentConvertToStudentDto studentConvertToStudentDto;

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
                powerBillDetail.setNumberOfMoneyMustPay(powerBillDto.getNumberOfMoneyMustPay());
                powerBillDetail.setPriceList(new PriceList(powerBillDto));
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
    public List<PowerBillDetail> getAllPowerBillByMaxEndDate() {
        List<Room> rooms = roomService.getAllRooms();
        List<PowerBillDto> powerBillDtos = powerBillRepositoryCustom.getAllPowerBillByMaxEndDate();
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
                powerBillDetail.setNumberOfMoneyMustPay(powerBillDto.getNumberOfMoneyMustPay());
                powerBillDetail.setPriceList(new PriceList(powerBillDto));
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
    public PowerBillDetail getAPowerBill(LocalDate date, Integer roomId) {
        List<PowerBillDetail> powerBillDtos = getAllDetailPowerBills(date);
        return powerBillDtos.stream().filter(powerBillDto -> powerBillDto.getDetailRoomDto().getId() == roomId)
                .findFirst().orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PowerBillDetail updatePowerBill(Integer roomId, PowerBillDetail powerBillDetail) throws BadRequestException {
        if (powerBillRepositoryCustom.updatePowerBill(roomId, powerBillDetail) <= 0) {
            throw new BadRequestException("Cannot implement update");
        }
        return powerBillDetail;
    }

    @Override
    public ByteArrayInputStream exportExcelFile(LocalDate currentDate) throws IOException {
        List<PowerBillDetail> powerBillDetails = getAllDetailPowerBills(currentDate);
        PowerBillExcelHelper<PowerBillDetail> powerBillExcelHelper = new PowerBillExcelHelper(powerBillDetails);
        powerBillExcelHelper.writeHeaderLine(StringUtil.HEADER_POWER_BILLS, StringUtil.SHEET_POWERBILL);
        powerBillExcelHelper.writeDataLines(powerBillDetail -> {
            int rowCount = 1;

            CellStyle style = powerBillExcelHelper.getWorkbook().createCellStyle();
            XSSFFont font = powerBillExcelHelper.getWorkbook().createFont();
            font.setFontHeight(14);
            style.setFont(font);
            for (PowerBillDetail powerBillDetaiCurrent : powerBillDetails) {
                Row row = powerBillExcelHelper.getSheet().createRow(rowCount++);
                int columnCount = 0;
                powerBillExcelHelper.createCell(row, columnCount++, powerBillDetaiCurrent.getDetailRoomDto().getId(), style);
                powerBillExcelHelper.createCell(row, columnCount++, powerBillDetaiCurrent.getDetailRoomDto().getName(), style);
                powerBillExcelHelper.createCell(row, columnCount++, DateUtil.getLDateFromSDate(powerBillDetaiCurrent.getStartDate()).toString(), style);
                powerBillExcelHelper.createCell(row, columnCount++, DateUtil.getLDateFromSDate(powerBillDetaiCurrent.getEndDate()).toString(), style);
                powerBillExcelHelper.createCell(row, columnCount++, powerBillDetaiCurrent.getNumberOfPowerBegin(), style);
                powerBillExcelHelper.createCell(row, columnCount++, powerBillDetaiCurrent.getNumberOfPowerEnd(), style);
                powerBillExcelHelper.createCell(row, columnCount++, powerBillDetaiCurrent.getNumberOfPowerUsed(), style);
                powerBillExcelHelper.createCell(row, columnCount++, powerBillDetaiCurrent.getNumberOfMoneyMustPay() + "đ", style);
                powerBillExcelHelper.createCell(row, columnCount++, powerBillDetaiCurrent.isPay() ? "x" : "--", style);
            }
        });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        powerBillExcelHelper.getWorkbook().write(outputStream);
        powerBillExcelHelper.getWorkbook().close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public void sendMail(String text, String subject, StudentDto studentDto, JavaMailSender javaMailSender, MimeMessage message, MimeMessageHelper helper) {
        try {
            helper.setTo(studentDto.getEmail());
            helper.setSubject(subject);
            helper.setText(text);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int importExcelFile(MultipartFile multipartFile, LocalDate localDate) {
        try {
            List<PowerBillImport> powerBillImports = PowerBillReadExcelHelper.parseExcelFile(multipartFile.getInputStream());
            List<PowerBillDetail> powerBillDetails = getAllPowerBillByMaxEndDate();
            return powerBillRepositoryCustom.insertPowerBills(powerBillDetails, powerBillImports);

        } catch (IOException e) {
            throw new RuntimeException("Cannot implement this action!!!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PowerBillDetail addPowerBill(Integer roomId, PowerBillDetail powerBillDetail) {
        if (powerBillRepositoryCustom.insertPowerBill(roomId, powerBillDetail) > 0) {
            return powerBillDetail;
        }
        throw new BadRequestException("Cannot implement the add power bill method!!!");
    }

    @Override
    public ByteArrayInputStream exportPDFPowerBillNew(PowerBillDetail powerBillDetail) {
        ExportPDFBill exportPDFBill = new ExportPDFBill(powerBillDetail);
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
                    infoStudent.add(new Paragraph("Số phòng:                " + powerBillDetail.getDetailRoomDto().getName(), font));

                    infoStudent.add(new Paragraph("Số tiền là:                " + Math.abs(powerBillDetail.getNumberOfMoneyMustPay()) + " đ" + " (Tiền điện)", font));
                    infoStudent.add(new Paragraph("\n", font));
                    infoStudent.add(new Paragraph("\n", font));
                    infoStudent.add(new Paragraph("                                                              Ngày thanh toán: " + LocalDate.now(), font));
                    infoStudent.add(new Paragraph("\n", font));
                    document.add(infoStudent);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
}
