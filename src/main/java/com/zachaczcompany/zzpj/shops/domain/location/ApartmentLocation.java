package com.zachaczcompany.zzpj.shops.domain.location;

import java.util.Objects;

public class ApartmentLocation extends LocationDecorator {
    private final String apartment;

    public ApartmentLocation(Location location, String apartment) {
        super(location);
        this.apartment = apartment;
    }

    @Override
    public String getName() {
        return super.getName() + " " + Objects.toString(apartment, "");
    }
}