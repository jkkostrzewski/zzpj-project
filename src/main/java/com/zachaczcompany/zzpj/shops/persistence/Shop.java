package com.zachaczcompany.zzpj.shops.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ShopDetails details;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ShopStats shopStats;

    public Shop(ShopDetails details, ShopStats shopStats) {
        this.details = details;
        this.shopStats = shopStats;
    }
}
