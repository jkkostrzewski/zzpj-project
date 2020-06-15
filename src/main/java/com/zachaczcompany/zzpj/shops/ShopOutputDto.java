package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.Address;
import com.zachaczcompany.zzpj.shops.domain.Localization;
import com.zachaczcompany.zzpj.shops.domain.Shop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopOutputDto {
    private long id;
    private String name;
    private Address address;
    private Localization localization;

    public ShopOutputDto(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.address = shop.getAddress();
        this.localization = shop.getLocalization();
    }
}
