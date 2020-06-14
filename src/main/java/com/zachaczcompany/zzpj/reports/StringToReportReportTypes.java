package com.zachaczcompany.zzpj.reports;

import org.springframework.core.convert.converter.Converter;

public class StringToReportReportTypes implements Converter<String, ReportTypes> {
    @Override
    public ReportTypes convert(String source) {
        try {
            return ReportTypes.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("There is not reports with extension %s", source));
        }
    }
}
