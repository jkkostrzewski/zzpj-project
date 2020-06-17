package com.zachaczcompany.zzpj.shops.domain.location;

public class ZipCodeLocation extends LocationDecorator {
    private final String zipCode;

    public ZipCodeLocation(Location location, String zipCode) {
        super(location);
        this.zipCode = zipCode;
    }

    @Override
    public String getName() {
        return super.getName() + ", " + zipCode;
    }
}