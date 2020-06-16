package com.zachaczcompany.zzpj.shops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;

@Entity
@NoArgsConstructor(access = PACKAGE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
class ShopDetails {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    @Getter
    private StockType stockType;

    @Embedded
    @Getter
    private Localization localization;

    @Embedded
    private OpenHours openHours;

    ShopDetails(StockType stockType, Localization localization, OpenHours openHours) {
        this.stockType = stockType;
        this.localization = localization;
        this.openHours = openHours;
    }

    Set<DailyOpenHours> getOpenHours() {
        return openHours.getOpenHours();
    }
}
