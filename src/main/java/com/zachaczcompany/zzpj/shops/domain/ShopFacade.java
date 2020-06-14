package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import com.zachaczcompany.zzpj.security.annotations.CanEditQueue;
import com.zachaczcompany.zzpj.shops.ShopOutputDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopFacade {
    private final ShopRepository shopRepository;
    private final ShopService shopService;
    private final ShopSearchRepository shopSearchRepository;

    @Autowired
    public ShopFacade(ShopRepository shopRepository, ShopService shopService,
                      ShopSearchRepository shopSearchRepository) {
        this.shopRepository = shopRepository;
        this.shopService = shopService;
        this.shopSearchRepository = shopSearchRepository;
    }

    public Iterable<ShopOutputDto> findAll(ShopFilterCriteria criteria, Pageable pageable) {
        Specification<Shop> spec = criteria.toSpecification();
        return shopRepository.findAll(spec, pageable).stream()
                             .peek(shop -> updateShopSearchStats(shop.getId(), criteria))
                             .map(ShopOutputDto::new)
                             .collect(Collectors.toList());
    }

    @CanEditQueue
    public Response updateShopStats(long id, StatisticsUpdateDto dto) {
        return validateId(id).flatMap(s -> shopService.updateShopStats(s, dto))
                             .fold(Function.identity(), Success::ok);
    }

    public Response findByShopId(long shopId) {
        return validateShopSearchId(shopId).fold(Function.identity(), Success::ok);
    }

    private Either<Error, ShopSearch> validateShopSearchId(long shopId) {
        return Option.ofOptional(shopSearchRepository.findById(shopId))
                     .toEither(Error.badRequest("SHOP_DOES_NOT_EXIST"));
    }

    private Either<Error, Shop> validateId(long id) {
        return Option.ofOptional(shopRepository.findById(id))
                     .toEither(Error.badRequest("SHOP_DOES_NOT_EXIST"));
    }

    private void updateShopSearchStats(Long shopId, ShopFilterCriteria criteria) {
        Optional<ShopSearch> shopSearch = shopSearchRepository.findByShopId(shopId);
        shopSearch.ifPresent(searchHistory -> {
            searchHistory.incrementValues(criteria);
            shopSearchRepository.save(searchHistory);
        });
    }
}
