package com.zachaczcompany.zzpj.location.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupermarketLocationDataResponse {
    private String name;
    private String lat;
    private String lon;
    private double distance;
}
