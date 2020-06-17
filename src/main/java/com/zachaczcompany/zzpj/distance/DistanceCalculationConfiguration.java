package com.zachaczcompany.zzpj.distance;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
class DistanceCalculationConfiguration {
    public final int limitOfRequests;
    public final double toleranceRatio;

    DistanceCalculationConfiguration(@Value("${locationiq.rest-api.numberOfRequests}") int limitOfRequests,
                                     @Value("${locationiq.rest-api.toleranceRatio}") double toleranceRatio) {
        this.limitOfRequests = limitOfRequests;
        this.toleranceRatio = toleranceRatio;
    }
}
