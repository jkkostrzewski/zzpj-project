package com.zachaczcompany.zzpj.shops.domain

import com.zachaczcompany.zzpj.shops.OpinionDto
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider.anyShop

class OpinionServiceSpec extends Specification {
    OpinionRepository opinionRepository = Mock(OpinionRepository)
    ShopFacade shopFacade = Mock(ShopFacade)
    OpinionService opinionService = new OpinionService(opinionRepository, shopFacade)

    def 'should not add opinion when shop does not exist'() {
        given: 'initialized opinionDto and shop'
        def dto = new OpinionDto(1, 3, "Ok")
        shopFacade.findShopById(dto.shopId) >> Optional.empty()
        opinionRepository.save(_ as Opinion) >> { Opinion o -> o }

        when: 'trying to add opinion for a shop that does not exist'
        def response = opinionService.addOpinion(dto)

        then:
        response.getCode() == 'OPINION_NOT_ADDED_SHOP_DOES_NOT_EXIST'
        response.getStatus() == HttpStatus.BAD_REQUEST
    }

    def 'should return error when shop does not exist'() {
        given: 'initialized shop'
        shopFacade.findShopById(1) >> Optional.empty()

        when: 'trying to get opinion for a shop that does not exist'
        def response = opinionService.getByShopId(1)

        then:
        response.getCode() == 'SHOP_DOES_NOT_EXIST'
        response.getStatus() == HttpStatus.BAD_REQUEST
    }

    def 'should return empty list when no opinion with such shop'() {
        given: 'initialized shop'
        shopFacade.findShopById(1) >> Optional.of(anyShop())
        opinionRepository.findByShop(_ as Shop) >> Collections.emptyList()

        when: 'trying to get opinion for shop'
        def response = opinionService.getByShopId(1)

        then:
        response.getContent() == Collections.emptyList()
        response.getStatus() == HttpStatus.OK
    }
}
