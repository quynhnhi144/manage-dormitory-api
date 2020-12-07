package com.managedormitory.helper;

import com.managedormitory.models.dto.room.DetailRoomDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class RoomExcelHelper {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<DetailRoomDto> detailRoomDtos;

    public RoomExcelHelper(List<DetailRoomDto> detailRoomDtos) {
        this.detailRoomDtos = detailRoomDtos;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Phòng");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        String currentDate = LocalDate.now().toString();

        createCell(row, 0, "ID Phòng", style);
        createCell(row, 1, "Tên phòng", style);
        createCell(row, 2, "Số lượng sinh viên", style);
        createCell(row, 3, "Loại phòng", style);
        createCell(row, 4, "Tên khu nhà", style);
        createCell(row, 5, "Tên quản lý", style);
        createCell(row, 6, "Đã trả tiền phòng", style);
        createCell(row, 7, "Đã trả tiền nước", style);
        createCell(row, 8, "Đã trả tiền xe", style);
        createCell(row, 10, "Ngày: " + currentDate, style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (DetailRoomDto detailRoomDto : detailRoomDtos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, detailRoomDto.getId(), style);
            createCell(row, columnCount++, detailRoomDto.getName(), style);
            createCell(row, columnCount++, detailRoomDto.getQuantityStudent(), style);
            createCell(row, columnCount++, detailRoomDto.getTypeRoom() != null ? detailRoomDto.getTypeRoom().getName() : null, style);
            createCell(row, columnCount++, detailRoomDto.getCampusName(), style);
            createCell(row, columnCount++, detailRoomDto.getUserManager(), style);
            createCell(row, columnCount++, detailRoomDto.getIsPayRoom() ? "x" : "--", style);
            createCell(row, columnCount++, detailRoomDto.getIsPayWaterBill() ? "x" : "--", style);
        }
    }

    public ByteArrayInputStream export() throws IOException {
        writeHeaderLine();
        writeDataLines();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
