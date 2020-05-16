package com.zachaczcompany.zzpj.shops.persistence;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Address {
    private String city;

    private String street;

    private int building;

    private String apartment;

    @Pattern(regexp = "\\d{2}-\\d{3}")
    private String zipCode;
}
