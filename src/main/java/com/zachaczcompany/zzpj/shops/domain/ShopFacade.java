package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import com.zachaczcompany.zzpj.security.annotations.CanEditQueue;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopOutputDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopFacade {
    private final ShopRepository shopRepository;
    private final ShopValidator validator;
    private final ShopService shopService;

    @Autowired
    public ShopFacade(ShopRepository shopRepository, ShopValidator validator, ShopService shopService) {
        this.shopRepository = shopRepository;
        this.validator = validator;
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
        return validator.shopExists(id)
                        .fold(Function.identity(), s -> Success.ok(shopService.updateShopStats(s, dto)));
    }

    public Either<Error, Shop> createShop(ShopCreateDto dto) {
        return validator.canCreateShop(dto)
                        .toEither()
                        .map(shopService::createShop);
    }
}
