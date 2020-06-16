package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.ZipCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import static lombok.AccessLevel.PACKAGE;

@Embeddable
@Getter
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor
public class Address {
    private String city;

    private String street;

    private int building;

    private String apartment;

    @Embedded
    private ZipCode zipCode;
}
