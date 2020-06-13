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

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopFacade {
    private final ShopRepository shopRepository;
    private final ShopService shopService;

    @Autowired
    public ShopFacade(ShopRepository shopRepository, ShopService shopService) {
        this.shopRepository = shopRepository;
        this.shopService = shopService;
    }

    public Iterable<ShopOutputDto> findAll(ShopFilterCriteria criteria, Pageable pageable) {
        Specification<Shop> spec = criteria.toSpecification();
        return shopRepository.findAll(spec, pageable).stream()
                .map(ShopOutputDto::new)
                .collect(Collectors.toList());
    }

    @CanEditQueue
    public Response updateShopStats(long id, StatisticsUpdateDto dto) {
        return validateId(id).flatMap(s -> shopService.updateShopStats(s, dto))
                .fold(Function.identity(), Success::ok);
    }

    private Either<Error, Shop> validateId(long id) {
        return Option.ofOptional(shopRepository.findById(id))
                .toEither(Error.badRequest("SHOP_DOES_NOT_EXIST"));
    }
}
