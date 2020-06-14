package com.zachaczcompany.zzpj.reports;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class PdfCreator implements ReportFileGenerator {
    private Document document;
    private PdfPTable table;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public PdfCreator() throws DocumentException {
        this.document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
    }

    public void createHeaderRow(io.vavr.collection.List<String> columnNames) {
        this.table = new PdfPTable(columnNames.length());
        columnNames
                .map(Paragraph::new)
                .map(PdfPCell::new)
                .forEach(e -> {
                    StyleCreator.basicStyle(e);
                    table.addCell(e);
                });
    }

    public RowBuilder addRow() {
        return new RowBuilder(table);
    }

    @Override
    public byte[] getReportBytes() throws IOException, DocumentException {
        document.add(table);
        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static class StyleCreator {
        static void basicStyle(PdfPCell cell) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
        }
    }

    public static final class RowBuilder implements IRowBuilder {
        private final PdfPTable table;

        private RowBuilder(PdfPTable table) {
            this.table = table;
        }

        public RowBuilder cell(int value) {
            PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(value)));
            StyleCreator.basicStyle(cell);
            table.addCell(cell);
            return this;
        }

        public RowBuilder cell(double value) {
            PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(value)));
            StyleCreator.basicStyle(cell);
            table.addCell(cell);
            return this;
        }

        public RowBuilder cell(String value) {
            PdfPCell cell = new PdfPCell(new Paragraph(value));
            StyleCreator.basicStyle(cell);
            table.addCell(cell);
            return this;
        }
    }
}
