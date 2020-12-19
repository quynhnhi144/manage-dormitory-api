package com.managedormitory.helper;

import com.managedormitory.models.dto.PowerBillImport;
import com.managedormitory.utils.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PowerBillReadExcelHelper {
    public static List<PowerBillImport> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet("Hóa đơn điện");
            Iterator rows = sheet.iterator();

            List<PowerBillImport> powerBillImports = new ArrayList<>();
            LocalDate localDate = LocalDate.now();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();
                PowerBillImport powerBillImport = new PowerBillImport();

                // skip header
                if (rowNumber == 0) {
                    Iterator cellsOfHeader = currentRow.iterator();
                    while (cellsOfHeader.hasNext()) {
                        Cell cell = (Cell) cellsOfHeader.next();
                        if (cell.getColumnIndex() == 3) {
                            localDate  = DateUtil.getLDateFromUtilDate(cell.getDateCellValue());
                        }
                    }
                    rowNumber++;
                    continue;
                }
                currentRow.getCell(3);
                    int cellIndex = 0;
                    Iterator cellsInRow = currentRow.iterator();
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = (Cell) cellsInRow.next();
                        if (cellIndex == 0) {
                            powerBillImport.setRoomName(currentCell.getStringCellValue());
                        } else if (cellIndex == 1) {
                            powerBillImport.setNumberOfPowerEnd((long) currentCell.getNumericCellValue());
                        }
                        powerBillImport.setEndDate(localDate);

                        cellIndex++;
                    }

                    powerBillImports.add(powerBillImport);
            }

            // close WorkBook
            workbook.close();
            return powerBillImports;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi!!! " + e.getMessage());
        }
    }
}
