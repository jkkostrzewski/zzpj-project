package com.zachaczcompany.zzpj.shops.domain

import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto
import org.springframework.http.HttpStatus
import spock.lang.Specification

class ShopServiceSpec extends Specification {
    ShopRepository shopRepository = Mock(ShopRepository)
    ShopService shopService = new ShopService(shopRepository)

    def 'should update shop stats successfully'() {
        given: 'shop and statistics update dto'
        def shop = ShopTestsDataProvider.shopWithStats maxCapacity: 20, peopleInside: 10, peopleInQueue: 0
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
        def shop = ShopTestsDataProvider.shopWithStats maxCapacity: 20, peopleInside: 10, peopleInQueue: 0
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
}
