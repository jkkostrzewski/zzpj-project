package com.zachaczcompany.zzpj.history.shopStats;

import com.zachaczcompany.zzpj.shops.domain.Shop;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShopStatsChangedEvent {
    public final Shop shop;
}
