package com.zachaczcompany.zzpj.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoutingResponse {
    private List<Route> routes;
}
