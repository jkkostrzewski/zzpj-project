package com.zachaczcompany.zzpj.reports;

public interface IRowBuilder {
    IRowBuilder cell(int value);

    IRowBuilder cell(double value);

    IRowBuilder cell(String value);
}
