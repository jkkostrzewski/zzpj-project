package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.Shop;
import lombok.Getter;

@Getter
public
class ShopWithDistanceOutputDto extends ShopOutputDto {
    private final double distance;
    private final boolean isAccurate;

    public ShopWithDistanceOutputDto(Shop shop, double distance, boolean accurate) {
        super(shop);
        this.distance = distance;
        this.isAccurate = accurate;
    }
}
