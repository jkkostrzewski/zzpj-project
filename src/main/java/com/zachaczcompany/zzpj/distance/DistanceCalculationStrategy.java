package com.zachaczcompany.zzpj.distance;

import com.zachaczcompany.zzpj.shops.domain.Localization;

public interface DistanceCalculationStrategy {
    double calculate(Localization from, Localization to);

    boolean isAccurate();
}
