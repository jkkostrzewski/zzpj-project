package com.zachaczcompany.zzpj.shops.distance;

import com.zachaczcompany.zzpj.shops.domain.Localization;

interface DistanceCalculationStrategy {
    double calculate(Localization from, Localization to);
}
