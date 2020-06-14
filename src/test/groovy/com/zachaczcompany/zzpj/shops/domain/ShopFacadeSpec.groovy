package com.zachaczcompany.zzpj.shops.domain

import com.zachaczcompany.zzpj.commons.response.Error
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider.anyShopSearch
import static com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider.anyShopFilterCriteria

class ShopFacadeSpec extends Specification {
    ShopSearchRepository shopSearchRepository = Mock(ShopSearchRepository)
    ShopRepository shopRepository = Mock(ShopRepository)
    ShopService shopService = new ShopService(shopRepository, shopSearchRepository) //TODO searchRepository will be deleted from this class
    ShopFacade shopFacade = new ShopFacade(shopRepository, shopService, shopSearchRepository)


    def 'should correctly update search statistics when executing findAll'() {
        given: 'initialized shopSearch'
        shopSearchRepository.findByShopId(1) >> Optional.of(anyShopSearch())
        shopSearchRepository.save(_ as ShopSearch) >> { ShopSearch ss -> ss }

        when: 'executing update on given shopSearch'
        shopFacade.updateShopSearchStats(1, anyShopFilterCriteria())

        then: 'shopSearch increments values and saves to repository'
        1 * shopSearchRepository.save(_ as ShopSearch);
    }

    def 'should return error if findByShopId gets id of nonexistent shop'() {
        given: 'repository with no elements'
        shopSearchRepository.findById(_ as Long) >> Optional.empty()

        when: 'trying to find ShopSearch for a shop that does not exist'
        def response = shopFacade.findByShopId((long)1) as Error

        then:
        response.getCode() == 'SHOP_DOES_NOT_EXIST'
        response.getStatus() == HttpStatus.BAD_REQUEST
    }

}
