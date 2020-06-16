package com.zachaczcompany.zzpj.shops.domain.location;

import java.util.Objects;

public class BuildingLocation extends LocationDecorator {
    private final String building;

    public BuildingLocation(Location location, String building) {
        super(location);
        this.building = building;
    }

    @Override
    public String getName() {
        return super.getName() + " " + Objects.toString(building, "");
    }
}