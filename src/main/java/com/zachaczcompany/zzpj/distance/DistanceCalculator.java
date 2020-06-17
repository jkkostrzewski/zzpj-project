package com.zachaczcompany.zzpj.distance;

import com.zachaczcompany.zzpj.shops.domain.Localization;

public class DistanceCalculator {
    private final DistanceCalculationStrategy strategy;

    public DistanceCalculator(DistanceCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateDistance(Localization from, Localization to){
        return from.distanceInMeters(to);
    }
}
