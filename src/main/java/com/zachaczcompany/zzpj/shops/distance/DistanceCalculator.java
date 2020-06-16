package com.zachaczcompany.zzpj.shops.distance;

import com.zachaczcompany.zzpj.shops.domain.Localization;

public class DistanceCalculator {
    private final DistanceCalculationStrategy preFilterStrategy;
    private final DistanceCalculationStrategy accurateStrategy;

    public DistanceCalculator(DistanceCalculationStrategy strategy, DistanceCalculationStrategy accurateStrategy) {
        this.preFilterStrategy = strategy;
        this.accurateStrategy = accurateStrategy;
    }

    public double inaccurateDistance(Localization from, Localization to) {
        return preFilterStrategy.calculate(from, to);
    }

    public double accurateDistance(Localization from, Localization to){
        return accurateStrategy.calculate(from, to);
    }
}
