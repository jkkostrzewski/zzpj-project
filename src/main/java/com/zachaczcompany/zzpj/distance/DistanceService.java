package com.zachaczcompany.zzpj.distance;

import com.zachaczcompany.zzpj.shops.domain.DistanceCriteria;
import com.zachaczcompany.zzpj.shops.domain.Localization;
import com.zachaczcompany.zzpj.shops.domain.Shop;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class DistanceService {
    private final DistanceCalculationConfiguration configuration;

    @Autowired
    public DistanceService(DistanceCalculationConfiguration configuration) {
        this.configuration = configuration;
    }

    public List<Tuple2<Shop, Double>> getClosestShops(List<Shop> shops, DistanceCalculationStrategy strategy, DistanceCriteria distanceCriteria) {
        var calculator = new DistanceCalculator(strategy);
        return shops.stream()
                    .map(shop -> toTupleWithDistance(shop, distanceCriteria, calculator::calculateDistance))
                    .filter(t -> isCloseEnough(t, distanceCriteria, getToleranceRatio(strategy)))
                    .sorted(Comparator.comparing(t -> t._2))
                    .limit(configuration.limitOfRequests)
                    .collect(Collectors.toUnmodifiableList());
    }

    private Tuple2<Shop, Double> toTupleWithDistance(Shop shop, DistanceCriteria criteria, BiFunction<Localization, Localization, Double> calculator) {
        var from = criteria.getLocalization();
        var to = shop.getLocalization();

        double calculated = calculator.apply(from, to);
        return Tuple.of(shop, calculated);
    }

    private boolean isCloseEnough(Tuple2<Shop, Double> tuple, DistanceCriteria criteria, double ratio) {
        return tuple._2 <= ratio * criteria.getRadius();
    }

    private double getToleranceRatio(DistanceCalculationStrategy strategy) {
        return strategy.isAccurate() ? 1.0 : configuration.toleranceRatio;
    }
}
