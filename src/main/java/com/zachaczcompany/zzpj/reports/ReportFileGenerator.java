package com.zachaczcompany.zzpj.reports;

public interface ReportFileGenerator {
    IRowBuilder addRow();

    byte[] getReportBytes();

    void createHeaderRow(io.vavr.collection.List<String> columnNames);
}
