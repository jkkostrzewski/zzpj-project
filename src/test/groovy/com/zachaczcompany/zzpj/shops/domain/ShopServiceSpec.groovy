package com.zachaczcompany.zzpj.shops.domain

import com.zachaczcompany.zzpj.location.integration.LocationRestService
import com.zachaczcompany.zzpj.shops.ShopCreateDto
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalTime

import static com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider.*

class ShopServiceSpec extends Specification {
    ShopRepository shopRepository = Mock(ShopRepository)
    ShopSearchRepository shopSearchRepository = Mock(ShopSearchRepository)
    LocationRestService locationRestService = Mock(LocationRestService)
    ApplicationEventPublisher eventPublisher = Mock(ApplicationEventPublisher)
    ShopService shopService = new ShopService(eventPublisher, shopRepository, shopSearchRepository, locationRestService, 2, toleranceRatio)

    def 'should get open hours from dto'() {
        given: 'shop create dto'
        def openFrom = LocalTime.of(7, 30)
        def openTo = LocalTime.of(21, 00)
        def openHours = dtoOpenHoursAlways(openFrom, openTo)
        def dto = shopCreateDtoWithOpenHours(openHours)

        when: 'service is converting dto to OpenHours entity'
        def openHoursEntity = shopService.getOpenHours(dto)

        then: 'OpenHours entity has been converted correctly'
        openHoursEntity.openHours.size() == 7
        openHoursEntity.openHours.each { assert it.openFrom == openFrom }
        openHoursEntity.openHours.each { assert it.openTo == openTo }
        def daysOfWeek = openHoursEntity.openHours.collect { it.dayOfWeek } as Set
        daysOfWeek == Set.of(DayOfWeek.values())
    }

    def 'should get open shop stats from dto'() {
        given: 'shop create dto'
        def maxCapacity = 150
        def dto = shopCreateDtoWithMaxCapacity(maxCapacity)

        when: 'service is retrieving shop stats from dto'
        def stats = shopService.getShopStats(dto)

        then: 'shop stats has been retrieved correctly'
        stats.maxCapacity == maxCapacity
        stats.peopleInQueue == 0
        stats.peopleInside == 0
    }

    def 'should get daily open hours from dto'() {
        given: 'dto daily open hours'
        def from = LocalTime.of(8, 00)
        def to = LocalTime.of(18, 00)
        def day = DayOfWeek.SUNDAY
        def daily = new ShopCreateDto.OpenHours(day, from, to)

        when: 'service is converting dto to DailyOpenHours'
        def converted = shopService.getDailyOpenHours(daily)

        then: 'dto has been converted successfully'
        converted.dayOfWeek == day
        converted.openFrom == from
        converted.openTo == to
    }

    def 'should get address from dto'() {
        given: 'shop create dto'
        def name = 'Sklep'
        def city = 'Miasto'
        def street = 'ul. Uliczna'
        def building = 123
        def apartment = '14a'
        def zipCode = '12-345'
        def dto = shopCreateDtoWithAddress(name, city, street, building, apartment, zipCode)

        when: 'service is retrieving address from dto'
        def address = shopService.getAddress(dto)

        then: 'address has been retrieved properly'
        address.city == dto.city
        address.street == dto.street
        address.building == dto.building
        address.apartment == dto.apartment
        address.zipCode.zipCode == dto.zipCode
    }

    def 'should create shop from dto'() {
        given: 'shop create dto'
        def dto = shopCreateDtoWithMaxCapacity(100)
        shopRepository.save(_ as Shop) >> { Shop shop -> shop }

        when: 'service is trying to create shop'
        def created = shopService.createShop(dto)

        then:
        created.name == dto.name

        def address = created.address
        address.city == dto.city
        address.street == dto.street
        address.apartment == dto.apartment
        address.building == dto.building
        address.zipCode.zipCode == dto.zipCode

        def stats = created.shopStats
        stats.peopleInside == 0
        stats.peopleInQueue == 0
        stats.maxCapacity == 100

        def localization = created.localization
        localization.latitude == dto.localization.latitude
        localization.longitude == dto.localization.longitude

        def openHours = created.details.openHours
        openHours.size() == 7
        openHours.each { assert it.openFrom == LocalTime.NOON }
        openHours.each { assert it.openTo == LocalTime.MIDNIGHT }

        def daysOfWeek = openHours.collect { it.dayOfWeek } as Set
        daysOfWeek == Set.of(DayOfWeek.values())
    }

    def 'should update shop stats successfully'() {
        given: 'shop and statistics update dto'
        def shop = shopWithStats(20, 10, 0)
        def dto = new StatisticsUpdateDto(joinedToQueue, wentInside, leftQueue, leftInside)
        shopRepository.save(_ as Shop) >> { Shop s -> s }

        when: 'service is updating stats'
        def response = shopService.updateShopStats(shop, dto)

        then: 'stats are updated correctly'
        response.isRight()
        def stats = response.get()
        stats.peopleInQueue == inQueue
        stats.peopleInside == inside

        where:
        joinedToQueue | wentInside | leftQueue | leftInside || inside | inQueue
        30            | 10         | 5         | 10         || 10     | 15
        10            | 5          | 5         | 0          || 15     | 0
        25            | 20         | 0         | 11         || 19     | 5
        1             | 0          | 0         | 10         || 0      | 1
        150           | 145        | 2         | 135        || 20     | 3
    }

    def 'should not update shop stats and return error'() {
        given: 'shop and statistics update dto'
        def shop = shopWithStats(20, 10, 0)
        def dto = new StatisticsUpdateDto(joinedToQueue, wentInside, leftQueue, leftInside)
        shopRepository.save(_ as Shop) >> { Shop s -> s }

        when: 'service is updating stats'
        def response = shopService.updateShopStats(shop, dto)

        then: 'stats are updated correctly'
        response.isLeft()
        def error = response.getLeft()
        error.code == 'CANNOT_UPDATE_STATS'
        error.status == HttpStatus.BAD_REQUEST

        where:
        joinedToQueue | wentInside | leftQueue | leftInside
        0             | 10         | 5         | 10
        0             | 0          | 0         | 11
        0             | 1          | 0         | 12
        5             | 15         | 0         | 5
        5             | 15         | 0         | 0
        20            | 20         | 0         | 5
    }

    def 'should correctly update search statistics when executing findAll'() {
        given: 'initialized shopSearch'
        shopSearchRepository.findByShopId(1) >> Optional.of(anyShopSearch())
        shopSearchRepository.save(_ as ShopSearch) >> { ShopSearch ss -> ss }

        when: 'executing update on given shopSearch'
        shopService.updateShopSearchStats(1, anyShopFilterCriteria())

        then: 'shopSearch increments values and saves to repository'
        1 * shopSearchRepository.save(_ as ShopSearch)
    }
}
