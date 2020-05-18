package com.zachaczcompany.zzpj.location.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutingResponse {
    private List<Route> routes;
}
