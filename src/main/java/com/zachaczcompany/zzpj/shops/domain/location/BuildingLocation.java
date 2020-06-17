package com.zachaczcompany.zzpj.shops.domain.location;

public class BuildingLocation extends LocationDecorator {
    private final String building;

    public BuildingLocation(Location location, String building) {
        super(location);
        this.building = building;
    }

    @Override
    public String getName() {
        return super.getName() + " " + building;
    }
}