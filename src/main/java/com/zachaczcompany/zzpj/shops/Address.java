package com.zachaczcompany.zzpj.shops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.PACKAGE;

@Embeddable
@Getter
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor
class Address {
    private String city;

    private String street;

    private int building;

    private String apartment;

    @Pattern(regexp = "\\d{2}-\\d{3}")
    private String zipCode;
}
