package com.zachaczcompany.zzpj.reports;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfCreator implements ReportFileGenerator {
    private Document document;
    private PdfPTable table;
    private int currentRow = 0;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public PdfCreator() {
        this.document = new Document();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
        } catch (DocumentException e) {
            throw new RuntimeException(e.getMessage());
        }
        document.open();
    }

    public void createHeaderRow(List<String> columnNames) {
        this.table = new PdfPTable(columnNames.size());
        columnNames
                .stream()
                .map(Paragraph::new)
                .map(PdfPCell::new)
                .forEach(e -> {
                    StyleCreator.headerStyle(e);
                    table.addCell(e);
                });
    }

    public RowBuilder addRow() {
        currentRow++;
        return new RowBuilder(table, currentRow % 2 == 0);
    }

    @Override
    public byte[] getReportBytes() {
        try {
            document.add(table);
        } catch (DocumentException e) {
            throw new RuntimeException(e.getMessage());
        }
        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static class StyleCreator {
        static void basicStyle(PdfPCell cell) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
        }

        static void headerStyle(PdfPCell cell) {
            basicStyle(cell);
            cell.setBackgroundColor(BaseColor.YELLOW);
        }

        static void evenRowStyle(PdfPCell cell) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }

        static void oddRowStyle(PdfPCell cell) {
            cell.setBackgroundColor(BaseColor.WHITE);
        }
    }

    public static final class RowBuilder implements IRowBuilder {
        private final PdfPTable table;
        private boolean isEvenRow;

        private RowBuilder(PdfPTable table, boolean isEvenRow) {
            this.table = table;
            this.isEvenRow = isEvenRow;
        }

        public RowBuilder cell(int value) {
            PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(value)));
            StyleCreator.basicStyle(cell);
            setRowStyle(cell);
            table.addCell(cell);
            return this;
        }

        public RowBuilder cell(double value) {
            PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(value)));
            StyleCreator.basicStyle(cell);
            setRowStyle(cell);
            table.addCell(cell);
            return this;
        }

        public RowBuilder cell(long value) {
            PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(value)));
            StyleCreator.basicStyle(cell);
            setRowStyle(cell);
            table.addCell(cell);
            return this;
        }

        public RowBuilder cell(String value) {
            PdfPCell cell = new PdfPCell(new Paragraph(value));
            StyleCreator.basicStyle(cell);
            setRowStyle(cell);
            table.addCell(cell);
            return this;
        }

        private void setRowStyle(PdfPCell cell) {
            if (isEvenRow) {
                StyleCreator.evenRowStyle(cell);
            } else {
                StyleCreator.oddRowStyle(cell);
            }
        }
    }
}
