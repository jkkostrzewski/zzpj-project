package com.zachaczcompany.zzpj.shops.domain

import com.zachaczcompany.zzpj.commons.ZipCode
import com.zachaczcompany.zzpj.shops.ShopCreateDto
import com.zachaczcompany.zzpj.shops.ShopOutputDto
import com.zachaczcompany.zzpj.shops.ShopUpdateDto
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

import java.time.DayOfWeek
import java.time.LocalTime
import java.util.stream.Collectors

class ShopTestsDataProvider {
    static Address anyAddress() {
        new Address("Warsaw", "Street", 15, "230", new ZipCode("05-610"))
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
        Shop shop = new Shop("Leadl", anyAddress(), anyShopDetails(), anyShopStats())
        shop.id = 1
        return shop
    }

    @NamedVariant
    static Shop shopWithStats(@NamedParam int maxCapacity, @NamedParam int peopleInside, @NamedParam int peopleInQueue) {
        def stats = new ShopStats(maxCapacity, peopleInside, peopleInQueue)
        stats.id = 1
        new Shop("Leadl", anyAddress(), anyShopDetails(), stats)
    }

    static ShopCreateDto shopCreateDtoWithOpenHours(List<ShopCreateDto.OpenHours> openHours) {
        ShopCreateDto.builder()
                .name('name')
                .city('city')
                .street('street')
                .building(1)
                .apartment('apartment')
                .zipCode('00-000')
                .stockType(StockType.FOOD)
                .localization(new Localization(0.0, 0.0))
                .openHours(openHours)
                .maxCapacity(100)
                .build()
    }

    static List<ShopCreateDto.OpenHours> anyDtoOpenHours() {
        dtoOpenHoursAlways(LocalTime.NOON, LocalTime.MIDNIGHT)
    }

    static List<ShopCreateDto.OpenHours> dtoOpenHoursAlways(LocalTime from, LocalTime to) {
        Arrays.stream(DayOfWeek.values())
                .map(day -> new ShopCreateDto.OpenHours(day, from, to))
                .collect(Collectors.toList())
    }

    static String named(String name = "Andrzej", String surname = "Lepper") {
        name + ' ' + surname
    }

    static ShopCreateDto shopCreateDtoWithMaxCapacity(int maxCapacity) {
        ShopCreateDto.builder()
                .name('name')
                .city('city')
                .street('street')
                .building(1)
                .apartment('apartment')
                .zipCode('00-000')
                .stockType(StockType.FOOD)
                .localization(new Localization(0.0, 0.0))
                .openHours(anyDtoOpenHours())
                .maxCapacity(maxCapacity)
                .build()
    }

    static ShopCreateDto shopCreateDtoWithAddress(String name, String city, String street, int building, String apartment, String zipCode) {
        ShopCreateDto.builder()
                .name(name)
                .city(city)
                .street(street)
                .building(building)
                .apartment(apartment)
                .zipCode(zipCode)
                .stockType(StockType.FOOD)
                .localization(new Localization(0.0, 0.0))
                .openHours(anyDtoOpenHours())
                .maxCapacity(100)
                .build()
    }

    static ShopSearch anyShopSearch() {
        new ShopSearch(anyShop().getId())
    }

    static ShopFilterCriteria anyShopFilterCriteria() {
        def criteria = new ShopFilterCriteria()
        criteria.setName("Leadl")
        return criteria
    }

    static ShopUpdateDto anyShopUpdateDto() {
        new ShopUpdateDto("NewName", StockType.ELECTRONIC, dtoOpenHoursAlways(LocalTime.of(12, 0), LocalTime.of(16, 0)))
    }

    static ShopUpdateDto shopUpdateDto(String name, StockType type, List<ShopCreateDto.OpenHours> openHours) {
        new ShopUpdateDto(name, type, openHours)
    }
}