package com.zachaczcompany.zzpj.shops.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceCriteria {
    private Double longitude;
    private Double latitude;
    private Short radius;

    @JsonIgnore
    public Localization getLocalization() {
        return new Localization(latitude, longitude);
    }
}
