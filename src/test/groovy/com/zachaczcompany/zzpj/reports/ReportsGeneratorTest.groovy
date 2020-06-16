package com.zachaczcompany.zzpj.reports


import com.zachaczcompany.zzpj.shops.domain.Opinion
import com.zachaczcompany.zzpj.shops.domain.OpinionService
import com.zachaczcompany.zzpj.shops.domain.ShopSearch
import com.zachaczcompany.zzpj.shops.domain.ShopSearchRepository
import com.zachaczcompany.zzpj.shops.domain.ShopTestsDataProvider
import spock.lang.Specification

class ReportsGeneratorTest extends Specification {
    ShopSearchRepository shopSearchRepository = Mock(ShopSearchRepository)
    OpinionService opinionService = Mock(OpinionService)
    ReportsGenerator reportsGenerator = new ReportsGenerator(shopSearchRepository, opinionService)
    List<Opinion> options = createOpinions()
    ShopSearch shopSearch = createShopSearch()

    def 'should return list of optional'() {
        given: 'opinions to fetch'
        opinionService.getByShopId(_) >> options;
        when:
        def optionsData = reportsGenerator.getOpinionsData(1L)
        then:
        optionsData.size() == 3
        optionsData.containsAll(options)
    }

    def 'should throw exception where no exists opinions about shop'() {
        given: 'empty list of opinions'
        opinionService.getByShopId(_) >> List.of()
        when:
        reportsGenerator.getOpinionsData(1L)
        then:
        thrown(ReportsGenerator.NoDataException)
    }

    def 'should return list of shopsearch'() {
        given: 'optional with shopsearch'
        shopSearchRepository.findByShopId(1L) >> Optional.of(shopSearch)
        when:
        def shopSearchData = reportsGenerator.getShopSearchData(1L)
        then:
        shopSearchEquals(shopSearchData, shopSearch)
    }

    def 'throw exception where no exists shopsearch of shop'() {
        given: 'empty optional'
        shopSearchRepository.findByShopId(1L) >> Optional.empty()
        when:
        reportsGenerator.getShopSearchData(1L)
        then:
        thrown(ReportsGenerator.NoDataException)
    }

    static def shopSearchEquals(ShopSearch s1, ShopSearch s2) {
        return s1.address == s2.address &&
                s1.isOpen == s2.isOpen &&
                s1.maxQueueLength == s2.maxQueueLength &&
                s1.name == s2.name &&
                s1.canEnter == s2.canEnter &&
                s1.maxCapacity == s2.maxCapacity &&
                s1.shopId == s2.shopId &&
                s1.stockType == s2.stockType
    }

    static def createShopSearch() {
        def shopSearch = new ShopSearch(1)
        shopSearch.address = 5
        shopSearch.name = 10

        return shopSearch
    }

    static def createOpinions() {
        def shop = ShopTestsDataProvider.anyShop()

        shop.id = 1
        def o1 = new Opinion(shop, 2, "bad")
        def o2 = new Opinion(shop, 3, "good")
        def o3 = new Opinion(shop, 5, "very good")

        return List.of(o1, o2, o3)
    }
}
