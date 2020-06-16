package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.ZipCode;
import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.history.shopStats.ShopStatsChangedEvent;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopStatsDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class ShopService {
    private final ApplicationEventPublisher eventPublisher;
    private final ShopSearchRepository searchRepository;
    private final ShopRepository repository;

    @Autowired
    ShopService(ApplicationEventPublisher eventPublisher, ShopRepository repository, ShopSearchRepository shopSearchRepository) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
        this.searchRepository = shopSearchRepository;
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
        var deltaInside = dto.getPeopleEnteredShop() - dto.getPeopleLeftShop();
        var deltaQueue = dto.getPeopleJoinedQueue() - dto.getPeopleLeftQueue() - dto.getPeopleEnteredShop();

        Function<Shop, Shop> save = repository::save;
        Function<Shop, ShopStatsDto> mapToDto = s -> new ShopStatsDto(s.getShopStats());
        var saveAndMap = save.andThen(mapToDto);

        return Try.of(() -> shop.updatePeople(deltaInside, deltaQueue))
                  .andThen(this::publishShopStatsChangedEvent)
                  .toEither(Error.badRequest("CANNOT_UPDATE_STATS"))
                  .map(saveAndMap);
    }

    private void publishShopStatsChangedEvent(Shop shop) {
        var event = new ShopStatsChangedEvent(shop);
        eventPublisher.publishEvent(event);
    }

    Shop createShop(ShopCreateDto dto) {
        var newShop = new Shop(dto.getName(), getAddress(dto), getDetails(dto), getShopStats(dto));
        var saved = repository.save(newShop);
        publishShopStatsChangedEvent(newShop);
        createSearch(saved);
        return saved;
    }

    private void createSearch(Shop shop) {
        var search = new ShopSearch(shop.getId());
        searchRepository.save(search);
    }

    void updateShopSearchStats(Long shopId, ShopFilterCriteria criteria) {
        Optional<ShopSearch> shopSearch = searchRepository.findByShopId(shopId);
        shopSearch.ifPresent(searchHistory -> {
            searchHistory.incrementValues(criteria);
            searchRepository.save(searchHistory);
        });
    }
}
