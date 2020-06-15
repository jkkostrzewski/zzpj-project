package com.zachaczcompany.zzpj.reports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class XlsxCreator implements ReportFileGenerator {
    private final static int CELL_WIDTH_24_CHARS = 24 * 256;

    private final CellStyle stringCellStyle;
    private final CellStyle doubleCellStyle;
    private final CellStyle integerCellStyle;

    private final Workbook excelWorkbook;
    private final Sheet sheet;
    private int currentRow;

    public XlsxCreator() {
        excelWorkbook = new XSSFWorkbook();
        sheet = excelWorkbook.createSheet();
        currentRow = 0;
        stringCellStyle = StylesCreator.stringCellStyle(excelWorkbook);
        doubleCellStyle = StylesCreator.doubleCellStyle(excelWorkbook);
        integerCellStyle = StylesCreator.integerCellStyle(excelWorkbook);
    }

    public byte[] getReportBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            this.excelWorkbook.write(stream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return stream.toByteArray();
    }

    public RowBuilder addRow() {
        currentRow++;
        Row row = sheet.createRow(currentRow);
        return new RowBuilder(row);
    }

    public void createHeaderRow(List<String> columnNames) {
        Row header = sheet.createRow(0);
        sheet.setColumnWidth(0, CELL_WIDTH_24_CHARS);

        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            sheet.setColumnWidth(columnIndex, CELL_WIDTH_24_CHARS);
            var cell = header.createCell(columnIndex);
            cell.setCellValue(columnNames.get(columnIndex));
            cell.setCellStyle(stringCellStyle);
        }
    }

    private static class StylesCreator {
        static CellStyle stringCellStyle(Workbook workbook) {
            CellStyle stringCellStyle = workbook.createCellStyle();
            stringCellStyle.setWrapText(true);
            return stringCellStyle;
        }

        static CellStyle doubleCellStyle(Workbook workbook) {
            CellStyle doubleCellStyle = workbook.createCellStyle();
            doubleCellStyle.setDataFormat(workbook.getCreationHelper()
                                                  .createDataFormat()
                                                  .getFormat("#,##0.00"));
            return doubleCellStyle;
        }

        static CellStyle integerCellStyle(Workbook workbook) {
            CellStyle integerCellStyle = workbook.createCellStyle();
            integerCellStyle.setDataFormat(workbook.getCreationHelper()
                                                   .createDataFormat()
                                                   .getFormat("#,##0"));
            return integerCellStyle;
        }
    }

    public final class RowBuilder implements IRowBuilder {
        private final Row row;
        int currentCell = 0;

        private RowBuilder(Row row) {
            this.row = row;
        }

        public RowBuilder cell(int value) {
            Cell cell = newCell();
            cell.setCellValue(value);
            cell.setCellStyle(integerCellStyle);
            return this;
        }

        public RowBuilder cell(long value) {
            Cell cell = newCell();
            cell.setCellValue(value);
            cell.setCellStyle(integerCellStyle);
            return this;
        }

        public RowBuilder cell(String value) {
            Cell cell = newCell();
            cell.setCellValue(value);
            cell.setCellStyle(stringCellStyle);
            return this;
        }

        public RowBuilder cell(double value) {
            Cell cell = newCell();
            cell.setCellValue(value);
            cell.setCellStyle(doubleCellStyle);
            return this;
        }

        private Cell newCell() {
            var cell = row.createCell(currentCell);
            currentCell++;
            return cell;
        }
    }
}