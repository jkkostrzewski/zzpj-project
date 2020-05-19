package com.zachaczcompany.zzpj.shops

import com.zachaczcompany.zzpj.shops.persistence.Address
import com.zachaczcompany.zzpj.shops.persistence.DailyOpenHours
import com.zachaczcompany.zzpj.shops.persistence.Localization
import com.zachaczcompany.zzpj.shops.persistence.OpenHours
import com.zachaczcompany.zzpj.shops.persistence.Shop
import com.zachaczcompany.zzpj.shops.persistence.ShopDetails
import com.zachaczcompany.zzpj.shops.persistence.ShopStats
import com.zachaczcompany.zzpj.shops.persistence.StockType

import java.time.DayOfWeek
import java.time.LocalTime
import java.util.stream.Collectors

class ShopTestsDataProvider {
    static Address anyAddress() {
        new Address("Warsaw", "Street", 15, "230", "05-610")
    }

    static DailyOpenHours anyDailyOpenHours() {
        new DailyOpenHours(DayOfWeek.FRIDAY, LocalTime.NOON, LocalTime.MIDNIGHT)
    }

    static Localization anyLocalization() {
        new Localization(-62.78471, 49.12860)
    }

    static Set<DailyOpenHours> anyDailyOpenHoursSet() {
        return Arrays.stream(DayOfWeek.values())
                .map(day -> new DailyOpenHours(day, LocalTime.NOON, LocalTime.MIDNIGHT))
                .collect(Collectors.toSet())
    }

    static OpenHours anyOpenHours() {
        new OpenHours(anyDailyOpenHoursSet())
    }

    static ShopDetails anyShopDetails() {
        new ShopDetails(StockType.DETERGENTS, anyLocalization(), anyOpenHours())
    }

    static ShopStats anyShopStats() {
        new ShopStats(13, 13, 4)
    }

    static Shop anyShop() {
        new Shop("Leadl", anyAddress(), anyShopDetails(), anyShopStats())
    }
}