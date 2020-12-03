package com.managedormitory.helper;

import com.managedormitory.utils.WriteDataLines;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.util.List;

public class PowerBillExcelHelper<E> {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<E> elements;

    public PowerBillExcelHelper(List<E> elements) {
        this.elements = elements;
        workbook = new XSSFWorkbook();
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public XSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public void writeHeaderLine(String[] strings, String sheetName) {
        sheet = workbook.createSheet(sheetName);

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        String currentDate = LocalDate.now().toString();

        for (int i = 0; i < strings.length; i++) {
            createCell(row, i, strings[i], style);
            if (i == strings.length - 1) {
                createCell(row, strings.length + 1, "Date: " + currentDate, style);
            }
        }
    }

    public void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public void writeDataLines(WriteDataLines<E> obj) {
        E e = elements.get(0);
        obj.writeDataLinesForObject(e);
    }
}
