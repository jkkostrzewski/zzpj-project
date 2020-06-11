package com.zachaczcompany.zzpj.raports;

import io.vavr.Tuple2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class SheetCreator {

    private final static int CELL_WIDTH_24_CHARS = 24 * 256;

    private final CellStyle stringCellStyle;
    private final CellStyle salaryCellStyle;
    private final CellStyle integerCellStyle;

    private final Workbook excelWorkbook;
    private final Sheet sheet;
    private final XSSFFont font;
    private int currentRow;

    public SheetCreator(String sheetName, io.vavr.collection.List<String> columnNames) {
        excelWorkbook = new XSSFWorkbook();
        sheet = excelWorkbook.createSheet(sheetName);
        currentRow = 0;
        font = StylesCreator.defaultFont(excelWorkbook);
        stringCellStyle = StylesCreator.stringCellStyle(excelWorkbook);
        salaryCellStyle = StylesCreator.salaryCellStyle(excelWorkbook);
        integerCellStyle = StylesCreator.integerCellStyle(excelWorkbook);

        createHeaderRow(columnNames);
    }

    public Workbook getStatisticsFile() {
        return excelWorkbook;
    }

    public RowBuilder addRow() {
        currentRow++;
        Row row = sheet.createRow(currentRow);
        return new RowBuilder(row);
    }

    private void createHeaderRow(io.vavr.collection.List<String> columnNames) {
        Row header = sheet.createRow(0);
        sheet.setColumnWidth(0, CELL_WIDTH_24_CHARS);

        columnNames.zipWithIndex(Tuple2::new)
                   .forEach(nameAndCellIndex -> {
                       var name = nameAndCellIndex._1;
                       var columnIndex = nameAndCellIndex._2;
                       sheet.setColumnWidth(columnIndex, CELL_WIDTH_24_CHARS);
                       var cell = header.createCell(columnIndex);
                       cell.setCellValue(name);
                       cell.setCellStyle(stringCellStyle);
                   });
    }

    private static class StylesCreator {
        static CellStyle stringCellStyle(Workbook workbook) {
            CellStyle stringCellStyle = workbook.createCellStyle();
            stringCellStyle.setWrapText(true);
            return stringCellStyle;
        }

        static CellStyle salaryCellStyle(Workbook workbook) {
            CellStyle salaryCellStyle = workbook.createCellStyle();
            salaryCellStyle.setDataFormat(workbook.getCreationHelper()
                                                  .createDataFormat()
                                                  .getFormat("#,##0.00"));
            return salaryCellStyle;
        }

        static CellStyle integerCellStyle(Workbook workbook) {
            CellStyle integerCellStyle = workbook.createCellStyle();
            integerCellStyle.setDataFormat(workbook.getCreationHelper()
                                                   .createDataFormat()
                                                   .getFormat("#,##0"));
            return integerCellStyle;
        }

        static XSSFFont defaultFont(Workbook workbook) {
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 12);
            return font;
        }
    }

    public final class RowBuilder {
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

        public RowBuilder cell(String value) {
            Cell cell = newCell();
            cell.setCellValue(value);
            cell.setCellStyle(stringCellStyle);
            return this;
        }

        private Cell newCell() {
            var cell = row.createCell(currentCell);
            currentCell++;
            return cell;
        }
    }
}