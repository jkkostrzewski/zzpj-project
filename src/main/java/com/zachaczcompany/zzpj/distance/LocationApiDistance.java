package com.zachaczcompany.zzpj.distance;

import com.zachaczcompany.zzpj.location.integration.LocationRestService;
import com.zachaczcompany.zzpj.location.model.Route;
import com.zachaczcompany.zzpj.shops.domain.Localization;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class LocationApiDistance implements DistanceCalculationStrategy {
    private final LocationRestService locationService;
    private final DistanceCalculationStrategy onFailureStrategy;

    public LocationApiDistance(LocationRestService locationService, DistanceCalculationStrategy onFailureStrategy) {
        this.locationService = locationService;
        this.onFailureStrategy = onFailureStrategy;
    }

    @Override
    public double calculate(Localization from, Localization to) {
        List<Route> fastestRoutes = locationService
                .getFastestRoute(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude())
                .getRoutes();

        if (CollectionUtils.isEmpty(fastestRoutes)) {
            return onFailureStrategy.calculate(from, to);
        }

        return fastestRoutes.stream()
                            .map(Route::getDistance)
                            .mapToDouble(d -> d)
                            .max()
                            .orElseThrow();
    }

    @Override
    public boolean isAccurate() {
        return true;
    }
}
