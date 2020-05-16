package com.zachaczcompany.zzpj.shops.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Getter
    private StockType stockType;

    @Embedded
    @Getter
    private Localization localization;

    @Embedded
    private OpenHours openHours;

    public ShopDetails(StockType stockType, Localization localization, OpenHours openHours) {
        this.stockType = stockType;
        this.localization = localization;
        this.openHours = openHours;
    }

    public Set<DailyOpenHours> getOpenHours() {
        return openHours.getOpenHours();
    }
}
