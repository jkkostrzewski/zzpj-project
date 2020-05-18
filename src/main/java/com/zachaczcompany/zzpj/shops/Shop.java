package com.zachaczcompany.zzpj.shops;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;

@Entity
@Getter
@NoArgsConstructor(access = PACKAGE)
class Shop {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @OneToOne(fetch = EAGER, cascade = ALL)
    private ShopDetails details;

    @OneToOne(fetch = EAGER, cascade = ALL)
    private ShopStats shopStats;

    Shop(String name, Address address, ShopDetails details, ShopStats shopStats) {
        this.name = name;
        this.address = address;
        this.details = details;
        this.shopStats = shopStats;
    }
}
