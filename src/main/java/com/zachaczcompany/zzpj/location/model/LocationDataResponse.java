package com.zachaczcompany.zzpj.location.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDataResponse {
    private String place_id;
    private String lat;
    private String lon;
    private String display_name;
}
