package com.zachaczcompany.zzpj;

import com.zachaczcompany.zzpj.shops.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
public class HelloController {

    private final ShopRepository shopRepository;

    @Autowired
    public HelloController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @GetMapping("shop")
    public Shop hello() {
        OpenHours openHours = new OpenHours(DailyOpenHours.always(LocalTime.NOON, LocalTime.MIDNIGHT));
        ShopDetails shopDetails = new ShopDetails(StockType.SERVICE, new Localization(), openHours);
        Shop shop = new Shop(shopDetails, new ShopStats());
        return shopRepository.save(shop);
    }
}
