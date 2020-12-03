package com.managedormitory.helper;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.utils.StringUtil;

import java.io.*;

public class ExportBillNewStudent {
    String pathToFont = "/font/arialuni.ttf";
    String fontName = ExportBillNewStudent.class.getResource(pathToFont).toString();
    private StudentNewDto studentNewDto;

    public ExportBillNewStudent(StudentNewDto studentNewDto) {
        this.studentNewDto = studentNewDto;
    }

    private void writeBillHeader(Document document) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 16, Font.BOLD);
        Paragraph preFace = new Paragraph();
        preFace.add(new Paragraph(StringUtil.KTXBK + "    " + StringUtil.CHXHCNVN, font));
        document.add(preFace);

        Paragraph p = new Paragraph(StringUtil.DLTDHP, font);
        p.setAlignment(Paragraph.ALIGN_RIGHT);
        p.add(new Paragraph("\n", font));
        document.add(p);

        Paragraph title = new Paragraph(StringUtil.TITLE, font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.add(new Paragraph("\n", font));
        document.add(title);
    }

    private void writeBillData(Document document, StudentNewDto studentNewDto) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 16);

        Paragraph infoStudent = new Paragraph();
        infoStudent.add(new Paragraph("Mã số sinh viên:        " + studentNewDto.getStudentDto().getIdCard(), font));

        infoStudent.add(new Paragraph("Họ và tên sinh viên:   " + studentNewDto.getStudentDto().getName(), font));

        infoStudent.add(new Paragraph("Số tiền là:                " + Math.abs(studentNewDto.getRoomBillDto().getPrice()) + " đ" + " (Tiền phòng) - " + Math.abs(studentNewDto.getWaterBillDto().getPrice()) + " đ" + " (Tiền nước)", font));
        infoStudent.add(new Paragraph("\n", font));
        document.add(infoStudent);
    }

    private void writeBillFooter(Document document) throws IOException, DocumentException {
        BaseFont baseFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 16, Font.BOLD);
        Font fontSmall = new Font(baseFont, 14);
        Paragraph p = new Paragraph();
        p.add(new Paragraph("                  Thủ quỹ " + "                                            " + "Tên sinh viên", font));
        p.add(new Paragraph("                   (Ký, họ tên) " + "                                                  " + " (Ký, họ tên) ", fontSmall));
        document.add(p);
    }

    public ByteArrayInputStream export(StudentNewDto studentNewDto) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            try {
                writeBillHeader(document);
                writeBillData(document, studentNewDto);
                writeBillFooter(document);
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
