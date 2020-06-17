package com.zachaczcompany.zzpj.shops.domain.location;

import java.util.Objects;

public class StreetLocation extends LocationDecorator {
    private final String street;

    public StreetLocation(Location location, String street) {
        super(location);
        this.street = street;
    }

    @Override
    public String getName() {
        return super.getName() + " " + Objects.toString(street, "");
    }
}