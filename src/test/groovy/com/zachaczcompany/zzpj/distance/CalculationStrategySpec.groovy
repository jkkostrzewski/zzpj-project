package com.zachaczcompany.zzpj.distance

import com.zachaczcompany.zzpj.location.integration.LocationRestService
import com.zachaczcompany.zzpj.shops.domain.Localization
import spock.lang.Specification

import static com.zachaczcompany.zzpj.distance.LocationTestDataProvider.responseWithDistances
import static com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider.anyLocalization

class CalculationStrategySpec extends Specification {
    LocationRestService locationService = Mock(LocationRestService)

    def 'should return value based on provided localizations'() {
        given: 'straight line strategy and two localizations'
        def strategy = new StraightLineDistance()

        def from = Mock(Localization) {
            distanceInMeters(_ as Localization) >> distance
        }
        def to = anyLocalization()

        when: 'strategy is calculating distance'
        def calculated = strategy.calculate(from, to)

        then: 'distance has been calculated'
        calculated == distance

        where:
        distance << [1.0, 2.0, 3.0, 4.0, 5.0]
    }

    def 'should return value based on location rest service'() {
        given: 'location strategy, two localizations and working location service'
        def strategy = new LocationApiDistance(locationService, null)

        def from = anyLocalization()
        def to = new Localization(154.0, -10.0)

        locationService.getFastestRoute(_, _, _, _) >> responseWithDistances([10, 100, 11, 121])

        when: 'strategy is trying to calculate distance'
        def result = strategy.calculate(from, to)

        then:
        result == 10.0 as double
    }
}