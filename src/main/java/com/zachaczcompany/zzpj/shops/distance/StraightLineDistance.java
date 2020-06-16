package com.zachaczcompany.zzpj.shops.distance;

import com.zachaczcompany.zzpj.shops.domain.Localization;

public class StraightLineDistance implements DistanceCalculationStrategy {

    @Override
    public double calculate(Localization from, Localization to) {
        return from.distanceInMeters(to);
    }
}