package com.zachaczcompany.zzpj.shops.domain;

import lombok.Data;

@Data
public class DistanceCriteria {
    private Double longitude;
    private Double latitude;
    private Short radius;

    public Localization getLocalization(){
        return new Localization(latitude, longitude);
    }
}
