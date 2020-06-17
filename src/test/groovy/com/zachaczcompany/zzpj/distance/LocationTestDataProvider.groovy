package com.zachaczcompany.zzpj.distance

import com.zachaczcompany.zzpj.location.model.Route
import com.zachaczcompany.zzpj.location.model.RoutingResponse

class LocationTestDataProvider {
    static responseWithDistances(List<Double> distances) {
        def routes = distances.collect { new Route(it as float, 0.0 as float) }
        new RoutingResponse(routes)
    }
}
