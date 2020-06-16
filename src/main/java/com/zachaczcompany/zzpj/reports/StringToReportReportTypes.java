package com.zachaczcompany.zzpj.reports;

import org.springframework.core.convert.converter.Converter;

public class StringToReportReportTypes implements Converter<String, ReportTypes> {
    @Override
    public ReportTypes convert(String source) {
            return ReportTypes.valueOf(source.toUpperCase());
    }
}
