package com.zachaczcompany.zzpj.location.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutingLocationDataResponse {
    private List<Route> routes;
}
