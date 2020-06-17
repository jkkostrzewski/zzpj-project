package com.zachaczcompany.zzpj.reports;

import java.util.List;

public interface ReportFileGenerator {
    IRowBuilder addRow();

    byte[] getReportBytes();

    void createHeaderRow(List<String> columnNames);
}
