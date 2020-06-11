package com.zachaczcompany.zzpj.raports;


import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfCreator {
    Document document;
    PdfPTable table;

    public PdfCreator(io.vavr.collection.List<String> columnNames) {
        Document document = new Document();
    }

    private void createHeaderRow(io.vavr.collection.List<String> columnNames) {
        PdfPTable table = new PdfPTable(columnNames.length());
        columnNames.forEach(table::addCell);
    }

    public RowBuilder addRow() {
        return new RowBuilder(table);
    }

    public final class RowBuilder {
        private final PdfPTable table;

        private RowBuilder(PdfPTable table) {
            this.table = table;
        }

        public RowBuilder cell(int value) {
            table.addCell(String.valueOf(value));
            return this;
        }

        public RowBuilder cell(double value) {
            table.addCell(String.valueOf(value));
            return this;
        }

        public RowBuilder cell(String value) {
            table.addCell(value);
            return this;
        }
    }

}
