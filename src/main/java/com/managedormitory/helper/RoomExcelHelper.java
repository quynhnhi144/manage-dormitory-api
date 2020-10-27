package com.managedormitory.helper;

import com.managedormitory.models.dto.RoomDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class RoomExcelHelper {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<RoomDto> roomDtos;

    public RoomExcelHelper(List<RoomDto> roomDtos) {
        this.roomDtos = roomDtos;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Rooms");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        String currentDate = LocalDate.now().toString();

        createCell(row, 0, "Room ID", style);
        createCell(row, 1, "Room Name", style);
        createCell(row, 2, "Quantity Student", style);
        createCell(row, 3, "Type Room", style);
        createCell(row, 4, "Campus Name", style);
        createCell(row, 5, "User Manager", style);
        createCell(row, 6, "Room Payment", style);
        createCell(row, 7, "Water Bill Payment", style);
        createCell(row, 8, "Vehicle Bill Payment", style);
        createCell(row, 9, "Power Bill Payment", style);
        createCell(row, 11, "Date: " + currentDate, style);
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

        for (RoomDto roomDto : roomDtos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, roomDto.getId(), style);
            createCell(row, columnCount++, roomDto.getName(), style);
            createCell(row, columnCount++, roomDto.getQuantityStudent(), style);
            createCell(row, columnCount++, roomDto.getTypeRoomName(), style);
            createCell(row, columnCount++, roomDto.getCampusName(), style);
            createCell(row, columnCount++, roomDto.getUserManager(), style);
            createCell(row, columnCount++, roomDto.getIsPayRoom(), style);
            createCell(row, columnCount++, roomDto.getIsPayWaterBill(), style);
            createCell(row, columnCount++, roomDto.getIsPayVehicleBill(), style);
            createCell(row, columnCount++, roomDto.getIsPayPowerBill(), style);
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
