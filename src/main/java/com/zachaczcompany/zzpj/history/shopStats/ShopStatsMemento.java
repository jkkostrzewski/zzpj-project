package com.zachaczcompany.zzpj.history.shopStats;

import com.zachaczcompany.zzpj.commons.DateTimeHelper;
import com.zachaczcompany.zzpj.shops.domain.Shop;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Access;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.Duration;
import java.time.LocalDateTime;

import static javax.persistence.AccessType.FIELD;

@Entity
@Access(FIELD)
@EqualsAndHashCode(of = "id")
@ToString
class ShopStatsMemento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private final Shop shop;

    private final int peopleInside;

    private final int peopleInQueue;

    private final LocalDateTime timestamp;

    ShopStatsMemento() {
        id = null;
        shop = null;
        peopleInQueue = 0;
        peopleInside = 0;
        timestamp = null;
    }

    ShopStatsMemento(Shop shop) {
        this.id = null;
        this.shop = shop;
        this.peopleInside = shop.getShopStats().getPeopleInside();
        this.peopleInQueue = shop.getShopStats().getPeopleInQueue();
        this.timestamp = DateTimeHelper.dateTime();
    }

    boolean isAfter(ShopStatsMemento other) {
        return this.timestamp.isAfter(other.timestamp);
    }

    boolean isBefore(ShopStatsMemento other) {
        return this.timestamp.isBefore((other.timestamp));
    }

    ShopStatsDiff diff(ShopStatsMemento other) {
        if (!shop.equals(other.shop)) {
            throw new IllegalArgumentException("Cannot compare stats of different shops!");
        }

        var timeDiff = Duration.between(other.timestamp, timestamp);
        var insideDiff = peopleInside - other.peopleInside;
        var queueDiff = peopleInQueue - other.peopleInQueue;
        return new ShopStatsDiff(shop.getId(), timeDiff, queueDiff, insideDiff);
    }
}
