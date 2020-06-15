package com.zachaczcompany.zzpj.reports;

import com.zachaczcompany.zzpj.shops.domain.Opinion;
import com.zachaczcompany.zzpj.shops.domain.ShopSearch;

import java.util.List;


public class FilesGenerator {
    public static byte[] getShopSearchFileBytes(List<ShopSearch> rows,
                                                ReportFileGenerator rfg) {
        List<String> columnNames = List.of("Shop id", "Address", "Name", "Stock type", "Is open",
                "Can Enter", "Max queue length", "Max Capacity");

        rfg.createHeaderRow(columnNames);
        rows.forEach(row -> {
            rfg
                    .addRow()
                    .cell(row.getShopId())
                    .cell(row.getAddress())
                    .cell(row.getName())
                    .cell(row.getStockType())
                    .cell(row.getIsOpen())
                    .cell(row.getCanEnter())
                    .cell(row.getMaxQueueLength())
                    .cell(row.getMaxCapacity());
        });
        return rfg.getReportBytes();
    }

    public static byte[] getOpinionsFileBytes(List<Opinion> rows,
                                              ReportFileGenerator rfg) {
        List<String> columnNames = List.of("Shop id", "Rate", "Description");

        rfg.createHeaderRow(columnNames);
        rows.forEach(row -> {
            rfg
                    .addRow()
                    .cell(row.getShop().getId())
                    .cell(row.getRate())
                    .cell(row.getDescription());
        });
        return rfg.getReportBytes();
    }
}
