package com.zachaczcompany.zzpj.reports;

public interface ReportFileGenerator {
    IRowBuilder addRow();

    byte[] getReportBytes() throws Exception;

    void createHeaderRow(io.vavr.collection.List<String> columnNames);
}
