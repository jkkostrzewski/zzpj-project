package com.zachaczcompany.zzpj.shops.domain.location;

public class LocationDecorator implements Location {
    private Location location;

    LocationDecorator(Location location) {
        this.location = location;
    }

    @Override
    public String getName() {
        return location.getName();
    }
}
