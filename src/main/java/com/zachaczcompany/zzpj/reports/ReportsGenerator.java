package com.zachaczcompany.zzpj.reports;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsGenerator {
    public byte[] getSearchStatistics(ReportTypes reportTypes) {
        io.vavr.collection.List<String> columnNames = io.vavr.collection.List.of("Elmpolee", "Conference", "Type");
        List<SearchStatisticRecord> rows = List
                .of(new SearchStatisticRecord(), new SearchStatisticRecord(), new SearchStatisticRecord());
        return FilesGenerator.getFileBytes(columnNames, rows, reportTypes.get());
    }
}
