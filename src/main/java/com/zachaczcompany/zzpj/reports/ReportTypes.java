package com.zachaczcompany.zzpj.reports;

import lombok.Getter;

public enum ReportTypes {
    XLSX(".xlsx") {
        @Override
        ReportFileGenerator get() {
            return new XlsxCreator();
        }
    },
    PDF(".pdf") {
        @Override
        ReportFileGenerator get() {
            return new PdfCreator();
        }
    };
    @Getter
    private String extension;

    ReportTypes(String extension) {
        this.extension = extension;
    }

    abstract ReportFileGenerator get();
}
