package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
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

    @Autowired
    ShopValidator(ShopRepository repository) {
        this.repository = repository;
    }

    Validation<Error, ShopCreateDto> canCreateShop(ShopCreateDto dto) {
        return openHoursValid(dto) ? Validation.valid(dto) : Validation.invalid(Error.badRequest("INVALID_OPEN_HOURS"));
    }

    private boolean openHoursValid(ShopCreateDto dto) {
        return coversWholeWeek(dto) && opensBeforeCloses(dto);
    }

    private boolean coversWholeWeek(ShopCreateDto dto) {
        List<DayOfWeek> daysCovered = dto.getOpenHours()
                                         .stream()
                                         .map(ShopCreateDto.OpenHours::getDayOfWeek)
                                         .collect(Collectors.toList());

        return daysCovered.size() == 7 && daysCovered.containsAll(Arrays.asList(DayOfWeek.values()));
    }

    private boolean opensBeforeCloses(ShopCreateDto dto) {
        return dto.getOpenHours().stream().allMatch(this::opensBeforeCloses);
    }

    private boolean opensBeforeCloses(ShopCreateDto.OpenHours openHours) {
        return openHours.getOpenFrom().isBefore(openHours.getOpenTo());
    }

    Validation<Error, Shop> shopExists(long id) {
        return Option.ofOptional(repository.findById(id))
                     .toValidation(Error.badRequest("SHOP_DOES_NOT_EXIST"));
    }
}
