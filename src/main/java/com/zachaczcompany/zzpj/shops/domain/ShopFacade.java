package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import com.zachaczcompany.zzpj.security.annotations.CanEditQueue;
import com.zachaczcompany.zzpj.security.annotations.CanEditShop;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopOutputDto;
import com.zachaczcompany.zzpj.shops.ShopUpdateDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import io.vavr.Tuple;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class ShopFacade {
    private final ShopRepository shopRepository;
    private final ShopValidator validator;
    private final ShopService service;

    @Autowired
    public ShopFacade(ShopRepository shopRepository, ShopService shopService,
                      ShopValidator validator) {
        this.shopRepository = shopRepository;
        this.validator = validator;
        this.service = shopService;
    }

    public Iterable<ShopOutputDto> findAll(ShopFilterCriteria criteria, Pageable pageable) {
        return service.findAll(criteria, pageable);
    }

    public Optional<Shop> findShopById(Long id) {
        return shopRepository.findById(id);
    }

    @CanEditQueue
    public Response updateShopStats(long id, StatisticsUpdateDto dto) {
        return validator.shopExists(id)
                        .toEither()
                        .flatMap(s -> service.updateShopStats(s, dto))
                        .fold(Function.identity(), Success::ok);
    }

    public Either<Error, Shop> createShop(ShopCreateDto dto) {
        return validator.canCreateShop(dto)
                        .toEither().flatMap(this::getShop);
    }

    private Either<Error, Shop> getShop(ShopCreateDto dto) {
        return Try.of(() -> service.createShop(dto)).toEither(Error.badRequest("LOCATION_NOT_PROVIDED_NOR_FOUND"));
    }

    public Response findByShopSearchId(long searchId) {
        return validator.searchExists(searchId).
                fold(Function.identity(), Success::ok);
    }

    @CanEditShop
    public Response updateShopDetails(long id, ShopUpdateDto dto) {
        return validator.canUpdateShop(dto)
                        .combine(validator.shopExists(id))
                        .ap(Tuple::of)
                        .fold(Error::concatCodes, tuple -> Success.ok(service.updateShopDetails(tuple._2, dto)));
    }
}
