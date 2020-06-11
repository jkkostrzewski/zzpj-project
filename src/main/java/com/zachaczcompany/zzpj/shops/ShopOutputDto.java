package com.zachaczcompany.zzpj.shops;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ShopOutputDto {
    private long id;
    private String name;
    private Address address;
    private Localization localization;

    ShopOutputDto(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.address = shop.getAddress();
        this.localization = shop.getDetails().getLocalization();
    }
}
