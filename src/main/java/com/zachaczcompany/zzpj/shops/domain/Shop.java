package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PACKAGE)
public class Shop {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private ShopDetails details;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private ShopStats shopStats;

    public Shop(String name, Address address, ShopDetails details, ShopStats shopStats) {
        this.name = name;
        this.address = address;
        this.details = details;
        this.shopStats = shopStats;
    }

    public Localization getLocalization() {
        return details.getLocalization();
    }

    public Shop updatePeople(int deltaPeopleInside, int deltaPeopleInQueue) throws IllegalShopOperation {
        shopStats.updatePeopleInside(deltaPeopleInside);
        shopStats.updatePeopleInQueue(deltaPeopleInQueue);
        return this;
    }

    public Shop updateShopNameAndDetails(String name, StockType type, OpenHours openHours) {
        if (!StringUtils.isEmpty(name)) {
            this.name = name;
        }
        details.updateDetails(type, openHours);
        return this;
    }
}
