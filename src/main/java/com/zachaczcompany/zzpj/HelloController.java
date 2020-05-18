package com.zachaczcompany.zzpj;

import com.zachaczcompany.zzpj.shops.persistence.Address;
import com.zachaczcompany.zzpj.shops.persistence.DailyOpenHours;
import com.zachaczcompany.zzpj.shops.persistence.Localization;
import com.zachaczcompany.zzpj.shops.persistence.OpenHours;
import com.zachaczcompany.zzpj.shops.persistence.Shop;
import com.zachaczcompany.zzpj.shops.persistence.ShopDetails;
import com.zachaczcompany.zzpj.shops.persistence.ShopRepository;
import com.zachaczcompany.zzpj.shops.persistence.ShopStats;
import com.zachaczcompany.zzpj.shops.persistence.StockType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Random;

@RestController
public class HelloController {

    private final ShopRepository shopRepository;

    @Autowired
    public HelloController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @GetMapping("shop")
    public Shop hello() {
        Address address = new Address("Łódź", "Łódzka", new Random().nextInt(100), "14a", "90-004");
        OpenHours openHours = new OpenHours(DailyOpenHours.always(LocalTime.NOON, LocalTime.MIDNIGHT));
        ShopDetails shopDetails = new ShopDetails(StockType.SERVICE, new Localization(new Random().nextDouble(), new Random().nextDouble()), openHours);
        ShopStats shopStats = new ShopStats(new Random().nextInt(100), new Random().nextInt(100), new Random().nextInt(100));
        Shop shop = new Shop("Sklep 1", address, shopDetails, shopStats);
        return shopRepository.save(shop);
    }
}
