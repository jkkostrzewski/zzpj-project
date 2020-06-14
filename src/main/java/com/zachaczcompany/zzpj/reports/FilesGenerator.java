package com.zachaczcompany.zzpj.reports;

import java.util.List;


public class FilesGenerator {
    public static byte[] getFileBytes(io.vavr.collection.List<String> columnNames,
                                      List<SearchStatisticRecord> rows,
                                      ReportFileGenerator rfg) {
        rfg.createHeaderRow(columnNames);
        rows.forEach(row -> {
            rfg
                    .addRow()
                    .cell(row.getD())
                    .cell(row.getI())
                    .cell(row.getS());
        });
        return rfg.getReportBytes();
    }
}
