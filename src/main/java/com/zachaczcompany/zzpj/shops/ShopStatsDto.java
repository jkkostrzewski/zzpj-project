package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.ShopStats;

public class ShopStatsDto {
    public final long id;
    public final int peopleInside;
    public final int peopleInQueue;

    public ShopStatsDto(ShopStats stats) {
        id = stats.getId();
        peopleInQueue = stats.getPeopleInQueue();
        peopleInside = stats.getPeopleInside();
    }
}
