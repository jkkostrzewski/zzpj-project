package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.ZipCode;
import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopOutputDto;
import com.zachaczcompany.zzpj.shops.ShopStatsDto;
import com.zachaczcompany.zzpj.shops.ShopUpdateDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class ShopService {
    private final ShopRepository repository;
    private final ShopSearchRepository searchRepository;

    @Autowired
    public ShopService(ShopRepository repository, ShopSearchRepository shopSearchRepository) {
        this.repository = repository;
        this.searchRepository = shopSearchRepository;
    }

    private static Address getAddress(ShopCreateDto dto) {
        return new Address(dto.getCity(), dto.getStreet(), dto.getBuilding(), dto.getApartment(), new ZipCode(dto
                .getZipCode()));
    }

    private static ShopDetails getDetails(ShopCreateDto dto) {
        return new ShopDetails(dto.getStockType(), dto.getLocalization(), getOpenHours(dto.getOpenHours()));
    }

    private static OpenHours getOpenHours(List<ShopCreateDto.OpenHours> openHours) {
        var daily = openHours.stream()
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
        var deltaInside = dto.getPeopleEnteredShop() - dto.getPeopleLeftShop();
        var deltaQueue = dto.getPeopleJoinedQueue() - dto.getPeopleLeftQueue() - dto.getPeopleEnteredShop();

        Function<Shop, Shop> save = repository::save;
        Function<Shop, ShopStatsDto> mapToDto = s -> new ShopStatsDto(s.getShopStats());
        var saveAndMap = save.andThen(mapToDto);

        return Try.of(() -> shop.updatePeople(deltaInside, deltaQueue))
                  .toEither(Error.badRequest("CANNOT_UPDATE_STATS"))
                  .map(saveAndMap);
    }

    public Either<Error, Shop> updateShopDetails(Shop shop, ShopUpdateDto dto) {
        List<ShopCreateDto.OpenHours> dtoOpenHours = dto.getOpenHours();
        OpenHours newOpenHours = dtoOpenHours != null ? getOpenHours(dtoOpenHours) : null;

        return Try.of(() -> shop.updateShopNameAndDetails(dto.getName(), dto.getStockType(), newOpenHours))
                  .toEither(Error.badRequest("CANNOT_UPDATE_DETAILS"))
                  .map(repository::save);
    }

    public Shop createShop(ShopCreateDto dto) {
        var newShop = new Shop(dto.getName(), getAddress(dto), getDetails(dto), getShopStats(dto));
        var saved = repository.save(newShop);
        createSearch(saved);
        return saved;
    }

    private ShopSearch createSearch(Shop shop) {
        var search = new ShopSearch(shop.getId());
        return searchRepository.save(search);
    }

    void updateShopSearchStats(Long shopId, ShopFilterCriteria criteria) {
        Optional<ShopSearch> shopSearch = searchRepository.findByShopId(shopId);
        shopSearch.ifPresent(searchHistory -> {
            searchHistory.incrementValues(criteria);
            searchRepository.save(searchHistory);
        });
    }
}
