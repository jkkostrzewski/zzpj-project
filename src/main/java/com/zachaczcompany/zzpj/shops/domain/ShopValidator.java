package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopUpdateDto;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
class ShopValidator {
    private final ShopRepository repository;
    private final ShopSearchRepository searchRepository;

    @Autowired
    ShopValidator(ShopRepository repository, ShopSearchRepository searchRepository) {
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    Validation<Error, ShopCreateDto> canCreateShop(ShopCreateDto dto) {
        return openHoursValid(dto.getOpenHours()) ?
                Validation.valid(dto) : Validation.invalid(Error.badRequest("INVALID_OPEN_HOURS"));
    }

    Validation<Error, ShopUpdateDto> canUpdateShop(ShopUpdateDto dto) {
        return openHoursValid(dto.getOpenHours()) ?
                Validation.valid(dto) : Validation.invalid(Error.badRequest("INVALID_OPEN_HOURS"));
    }

    private boolean openHoursValid(List<ShopCreateDto.OpenHours> openHours) {
        return coversWholeWeek(openHours) && opensBeforeCloses(openHours);
    }

    private boolean coversWholeWeek(List<ShopCreateDto.OpenHours> openHours) {
        List<DayOfWeek> daysCovered = openHours.stream()
                                               .map(ShopCreateDto.OpenHours::getDayOfWeek)
                                               .collect(Collectors.toList());

        return daysCovered.size() == 7 && daysCovered.containsAll(Arrays.asList(DayOfWeek.values()));
    }

    private boolean opensBeforeCloses(List<ShopCreateDto.OpenHours> openHours) {
        return openHours.stream().allMatch(this::opensBeforeCloses);
    }

    private boolean opensBeforeCloses(ShopCreateDto.OpenHours openHours) {
        return openHours.getOpenFrom().isBefore(openHours.getOpenTo());
    }

    Validation<Error, Shop> shopExists(long id) {
        return Option.ofOptional(repository.findById(id))
                     .toValidation(Error.badRequest("SHOP_DOES_NOT_EXIST"));
    }

    Either<Error, ShopSearch> searchExists(long shopId) {
        return Option.ofOptional(searchRepository.findById(shopId))
                     .toEither(Error.badRequest("SHOP_DOES_NOT_EXIST"));
    }
}
