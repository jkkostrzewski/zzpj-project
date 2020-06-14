package com.zachaczcompany.zzpj.reports;

public enum ReportTypes {
    XSLX() {
        @Override
        ReportFileGenerator get() {
            return new XslxCreator();
        }
    },
    PDF() {
        @Override
        ReportFileGenerator get() throws Exception {
            return new PdfCreator();
        }
    };

    abstract ReportFileGenerator get() throws Exception;
}
