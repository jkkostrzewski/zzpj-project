package com.zachaczcompany.zzpj.shops.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ShopDetails details;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ShopStats shopStats;

    public Shop(String name, Address address, ShopDetails details, ShopStats shopStats) {
        this.name = name;
        this.address = address;
        this.details = details;
        this.shopStats = shopStats;
    }
}
