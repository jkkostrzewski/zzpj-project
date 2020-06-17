package com.zachaczcompany.zzpj.shops.domain

import com.zachaczcompany.zzpj.commons.response.Error
import com.zachaczcompany.zzpj.distance.DistanceService
import com.zachaczcompany.zzpj.location.integration.LocationRestService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import spock.lang.Specification

class ShopFacadeSpec extends Specification {
    ShopSearchRepository shopSearchRepository = Mock(ShopSearchRepository)
    ShopRepository shopRepository = Mock(ShopRepository)
    LocationRestService locationRestService = Mock(LocationRestService)
    ShopValidator shopValidator = new ShopValidator(shopRepository, shopSearchRepository)
    ShopFacade shopFacade = new ShopFacade(shopRepository, shopService, shopValidator)
    ApplicationEventPublisher eventPublisher = Mock(ApplicationEventPublisher)
    DistanceService distanceService = Mock(DistanceService)
    NotificationService notificationService = Mock(NotificationService)
    ShopService shopService = new ShopService(eventPublisher, shopRepository, shopSearchRepository, locationRestService, distanceService, notificationService)

    def 'should return error if findByShopId gets id of nonexistent shop'() {
        given: 'repository with no elements'
        shopSearchRepository.findById(_ as Long) >> Optional.empty()

        when: 'trying to find ShopSearch for a shop that does not exist'
        def response = shopFacade.findByShopSearchId((long) 1) as Error

        then:
        response.getCode() == 'SHOP_DOES_NOT_EXIST'
        response.getStatus() == HttpStatus.BAD_REQUEST
    }

}
