package com.zachaczcompany.zzpj.shops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PACKAGE;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PACKAGE)
class Localization {
    private double latitude;
    private double longitude;
}
