package com.zachaczcompany.zzpj.distance


import io.vavr.Tuple2
import spock.lang.Specification

import static com.zachaczcompany.zzpj.distance.DistanceTestDataProvider.*
import static com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider.anyShop
import static com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider.shopWithLocalization

class DistanceServiceSpec extends Specification {
    static final TOLERANCE_RATIO = 1.5
    static final REQUESTS_LIMIT = 2
    DistanceCalculationConfiguration configuration = new DistanceCalculationConfiguration(REQUESTS_LIMIT, TOLERANCE_RATIO)
    DistanceService service = new DistanceService(configuration)

    def 'should return tolerance ratio based on provided strategy'() {
        when: 'service gets tolerance ratio based on strategy'
        def toleranceRatio = service.getToleranceRatio(calculationStrategy)

        then:
        toleranceRatio == expectedRatio as double

        where:
        calculationStrategy  || expectedRatio
        accurateStrategy()   || 1.0
        inaccurateStrategy() || TOLERANCE_RATIO
    }

    def 'should return true when shop is close enough according to criteria'() {
        given: 'distance criteria, shop and distance to shop'
        def distanceCriteria = distanceCriteriaWithRadius(500 as short)
        def tuple = Tuple2.of(shop, distance as Double)
        when: 'service is verifying if shop is close enough'
        def closeEnough = service.isCloseEnough(tuple, distanceCriteria, 1.0)

        then:
        closeEnough == expectedCloseEnough

        where:
        shop      | distance                 || expectedCloseEnough
        anyShop() | 501.0                    || false
        anyShop() | 499.0                    || true
        anyShop() | 0.0                      || true
        anyShop() | Double.POSITIVE_INFINITY || false
    }

    def 'should return tuple of shop and distance to desired localization'() {
        given: 'shop, distance criteria and distance calculator'
        def calculator = (l1, l2) -> 15.0 as double
        def shop = anyShop()

        when: 'service is calculating distance from'
        def tuple = service.toTupleWithDistance(shop, distanceCriteria, calculator)

        then: 'tuple of provided shop and distance has been returned'
        tuple._1 == shop
        tuple._2 == expectedDistance

        where:
        distanceCriteria                             || expectedDistance
        distanceCriteriaWithLocalization(1.0, 1.0)   || 15.0
        distanceCriteriaWithLocalization(10.0, 30)   || 15.0
        distanceCriteriaWithLocalization(1.0, -31.0) || 15.0
        distanceCriteriaWithLocalization(41.0, 1.0)  || 15.0
        distanceCriteriaWithLocalization(50, 51.0)   || 15.0
    }

    def 'should return closest shops'() {
        given: 'list of all shops, calculation strategy, distance criteria'
        def strategy = alwaysReturnTenStrategy()
        def criteria = distanceCriteriaWithRadius(999999 as short)

        when: 'service is trying to order shops by distance'
        def ordered = service.getClosestShops(shops, strategy, criteria)

        then: 'shops has been ordered correctly'
        ordered.size() == REQUESTS_LIMIT
        ordered[0]._1 == expectedOrder[0]
        ordered[1]._1 == expectedOrder[1]

        where:
        shops                                                                                              || expectedOrder
        [shopWithLocalization(0.0, 0.0), shopWithLocalization(1.0, 1.0), shopWithLocalization(10.0, 10.0)] || [shopWithLocalization(0.0, 0.0), shopWithLocalization(1.0, 1.0)]
    }
}
