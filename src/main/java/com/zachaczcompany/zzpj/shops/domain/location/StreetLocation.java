package com.zachaczcompany.zzpj.shops.domain.location;

public class StreetLocation extends LocationDecorator {
    private final String street;

    public StreetLocation(Location location, String street) {
        super(location);
        this.street = street;
    }

    @Override
    public String getName() {
        return super.getName() + " " + street;
    }
}