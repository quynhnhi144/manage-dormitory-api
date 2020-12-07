package com.managedormitory.helper;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.*;
import com.managedormitory.utils.StringUtil;
import com.managedormitory.utils.WriteDataLines;

import java.io.*;

public class ExportPDFBill<E> {
    private String fontName = ExportPDFBill.class.getResource(StringUtil.PATH_TO_FONT).toString();
    private E objPrint;

    public ExportPDFBill(E objPrint) {
        this.objPrint = objPrint;
    }

    public String getFontName() {
        return fontName;
    }

    public void writeBillHeader(Document document) throws DocumentException, IOException {
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

    public void writeBillData(WriteDataLines<E> condition) throws DocumentException, IOException {
        condition.writeDataLinesForObject(this.objPrint);
    }

    public void writeBillFooter(Document document) throws IOException, DocumentException {
        BaseFont baseFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 16, Font.BOLD);
        Font fontSmall = new Font(baseFont, 14);
        Paragraph p = new Paragraph();
        p.add(new Paragraph("                  Thủ quỹ " + "                                            " + "Tên sinh viên", font));
        p.add(new Paragraph("                   (Ký, họ tên) " + "                                                  " + " (Ký, họ tên) ", fontSmall));
        document.add(p);
    }

}
