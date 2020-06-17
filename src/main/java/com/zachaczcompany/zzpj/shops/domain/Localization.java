package com.zachaczcompany.zzpj.shops.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PACKAGE;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PACKAGE)
public class Localization {
    private double latitude;
    private double longitude;

    private final static int EARTH_RADIUS = 6371;

    public double distanceInMeters(Localization other) {
        return distanceInMeters(this, other);
    }

    public static double distanceInMeters(Localization from, Localization to) {
        double deltaLat = Math.toRadians(to.latitude - from.latitude);
        double deltaLon = Math.toRadians(to.longitude - from.longitude);
        double sinDeltaLat = Math.sin(deltaLat / 2);
        double sinDeltaLon = Math.sin(deltaLon / 2);
        double latRad = Math.toRadians(from.latitude);
        double otherLatRad = Math.toRadians(to.latitude);
        double a = sinDeltaLat * sinDeltaLat + sinDeltaLon * sinDeltaLon * Math.cos(latRad) * Math.cos(otherLatRad);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return c * EARTH_RADIUS;
    }
}
