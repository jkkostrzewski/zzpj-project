package com.zachaczcompany.zzpj.shops.domain.location;

import java.util.Objects;

public class CityLocation extends LocationDecorator {
    private final String city;

    public CityLocation(Location location, String city) {
        super(location);
        this.city = city;
    }

    @Override
    public String getName() {
        return super.getName() + " " + Objects.toString(city, "");
    }
}