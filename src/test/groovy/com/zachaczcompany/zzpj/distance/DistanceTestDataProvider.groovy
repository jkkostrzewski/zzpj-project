package com.zachaczcompany.zzpj.distance

import com.zachaczcompany.zzpj.shops.domain.DistanceCriteria
import com.zachaczcompany.zzpj.shops.domain.Localization

class DistanceTestDataProvider {
    static DistanceCalculationStrategy accurateStrategy() {
        new DistanceCalculationStrategy() {
            @Override
            double calculate(Localization from, Localization to) {
                return 0
            }

            @Override
            boolean isAccurate() {
                return true
            }
        }
    }

    static DistanceCalculationStrategy inaccurateStrategy() {
        new DistanceCalculationStrategy() {
            @Override
            double calculate(Localization from, Localization to) {
                return 0
            }

            @Override
            boolean isAccurate() {
                return false
            }
        }
    }

    static DistanceCriteria distanceCriteriaWithRadius(short distance) {
        new DistanceCriteria(25.0, 25.0, distance)
    }

    static DistanceCriteria distanceCriteriaWithLocalization(double lon, double lat) {
        new DistanceCriteria(lon, lat, 1500 as short)
    }

    static DistanceCalculationStrategy alwaysReturnTenStrategy() {
        new DistanceCalculationStrategy() {
            @Override
            double calculate(Localization from, Localization to) {
                return 10.0
            }

            @Override
            boolean isAccurate() {
                return true
            }
        }
    }
}
