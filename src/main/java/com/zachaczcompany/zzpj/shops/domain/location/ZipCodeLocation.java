package com.zachaczcompany.zzpj.shops.domain.location;

import java.util.Objects;

public class ZipCodeLocation extends LocationDecorator {
    private final String zipCode;

    public ZipCodeLocation(Location location, String zipCode) {
        super(location);
        this.zipCode = zipCode;
    }

    @Override
    public String getName() {
        return super.getName() + Objects.toString(", " + zipCode, "");
    }
}