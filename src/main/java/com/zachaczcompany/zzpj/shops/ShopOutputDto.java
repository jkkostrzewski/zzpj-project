package com.zachaczcompany.zzpj.shops;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
class ShopOutputDto {
    private long id;
    private String name;
    private Address address;
    private Localization localization;
    private List<Opinion> opinions;

    ShopOutputDto(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.address = shop.getAddress();
        this.localization = shop.getDetails().getLocalization();
        this.opinions = shop.getOpinions();
    }
}
