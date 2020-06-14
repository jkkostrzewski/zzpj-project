package com.zachaczcompany.zzpj.shops.domain

import com.zachaczcompany.zzpj.shops.domain.Address
import com.zachaczcompany.zzpj.shops.domain.DailyOpenHours
import com.zachaczcompany.zzpj.shops.domain.Localization
import com.zachaczcompany.zzpj.shops.domain.OpenHours
import com.zachaczcompany.zzpj.shops.domain.Shop
import com.zachaczcompany.zzpj.shops.domain.ShopDetails
import com.zachaczcompany.zzpj.shops.domain.ShopStats
import com.zachaczcompany.zzpj.shops.domain.StockType
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

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
        Shop shop =  new Shop("Leadl", anyAddress(), anyShopDetails(), anyShopStats())
        shop.id = 1
        return shop
    }

    @NamedVariant
    static Shop shopWithStats(@NamedParam int maxCapacity, @NamedParam int peopleInside, @NamedParam int peopleInQueue) {
        def stats = new ShopStats(maxCapacity, peopleInside, peopleInQueue)
        stats.id = 1
        new Shop("Leadl", anyAddress(), anyShopDetails(), stats)
    }

    static ShopSearch anyShopSearch() {
        new ShopSearch(anyShop().getId())
    }

    static ShopFilterCriteria anyShopFilterCriteria() {
        def criteria = new ShopFilterCriteria()
        criteria.setName("Leadl")
        return criteria
    }
}