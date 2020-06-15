package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.ZipCode;
import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopStatsDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class ShopService {
    private final ShopRepository repository;
    private final ShopSearchRepository shopSearchRepository;  //TODO usunąć razem z clr()

    @Autowired
    public ShopService(ShopRepository repository, ShopSearchRepository shopSearchRepository) {
        this.repository = repository;
        this.shopSearchRepository = shopSearchRepository;
    }

    private static Address getAddress(ShopCreateDto dto) {
        return new Address(dto.getCity(), dto.getStreet(), dto.getBuilding(), dto.getApartment(), new ZipCode(dto
                .getZipCode()));
    }

    private static ShopDetails getDetails(ShopCreateDto dto) {
        return new ShopDetails(dto.getStockType(), dto.getLocalization(), getOpenHours(dto));
    }

    private static OpenHours getOpenHours(ShopCreateDto dto) {
        var daily = dto.getOpenHours()
                       .stream()
                       .map(ShopService::getDailyOpenHours)
                       .collect(Collectors.toSet());
        return new OpenHours(daily);
    }

    private static DailyOpenHours getDailyOpenHours(ShopCreateDto.OpenHours openHours) {
        var day = openHours.getDayOfWeek();
        var from = openHours.getOpenFrom();
        var to = openHours.getOpenTo();
        return new DailyOpenHours(day, from, to);
    }

    private static ShopStats getShopStats(ShopCreateDto dto) {
        return new ShopStats(dto.getMaxCapacity(), 0, 0);
    }

    @Transactional(rollbackOn = IllegalShopOperation.class)
    public Either<Error, ShopStatsDto> updateShopStats(Shop shop, StatisticsUpdateDto dto) {
        var deltaInside = dto.getPeopleWentInside() - dto.getPeopleLeftInside();
        var deltaQueue = dto.getPeopleJoinedQueue() - dto.getPeopleLeftQueue() - dto.getPeopleWentInside();

        Function<Shop, Shop> save = repository::save;
        Function<Shop, ShopStatsDto> mapToDto = s -> new ShopStatsDto(s.getShopStats());
        var saveAndMap = save.andThen(mapToDto);

        return Try.of(() -> shop.updatePeople(deltaInside, deltaQueue))
                  .toEither(Error.badRequest("CANNOT_UPDATE_STATS"))
                  .map(saveAndMap);
    }

    public Shop createShop(ShopCreateDto dto) {
        var newShop = new Shop(dto.getName(), getAddress(dto), getDetails(dto), getShopStats(dto));
        return repository.save(newShop);
}
