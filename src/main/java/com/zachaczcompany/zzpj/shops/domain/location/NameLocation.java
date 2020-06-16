package com.zachaczcompany.zzpj.shops.domain.location;

public class NameLocation implements Location {
    private final String name;

    public NameLocation(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}