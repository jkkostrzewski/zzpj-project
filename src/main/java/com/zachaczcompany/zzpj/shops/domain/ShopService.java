package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.ZipCode;
import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.distance.DistanceCalculationStrategy;
import com.zachaczcompany.zzpj.distance.DistanceService;
import com.zachaczcompany.zzpj.distance.LocationApiDistance;
import com.zachaczcompany.zzpj.distance.StraightLineDistance;
import com.zachaczcompany.zzpj.history.shopStats.ShopStatsChangedEvent;
import com.zachaczcompany.zzpj.location.integration.LocationRestService;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopOutputDto;
import com.zachaczcompany.zzpj.shops.ShopStatsDto;
import com.zachaczcompany.zzpj.shops.ShopUpdateDto;
import com.zachaczcompany.zzpj.shops.ShopWithDistanceOutputDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import com.zachaczcompany.zzpj.shops.exceptions.LocationNotFoundException;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class ShopService {
    public static final double NOTIFIER_PEOPLE_MULTIPLIER = 0.75;

    private final ApplicationEventPublisher eventPublisher;
    private final ShopSearchRepository searchRepository;
    private final DistanceService distanceService;
    private final ShopRepository repository;
    private final LocationRestService locationService;
    private final NotificationService notificationService;

    @Autowired
    ShopService(ApplicationEventPublisher eventPublisher, ShopRepository repository,
                ShopSearchRepository shopSearchRepository, LocationRestService locationService, DistanceService distanceService,
                NotificationService notificationService) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
        this.searchRepository = shopSearchRepository;
        this.locationService = locationService;
        this.distanceService = distanceService;
        this.notificationService = notificationService;
    }

    private static Address getAddress(ShopCreateDto dto) {
        return new Address(dto.getCity(), dto.getStreet(), dto.getBuilding(),
                Objects.toString(dto.getApartment(), ""),
                new ZipCode(dto.getZipCode()));
    }

    private ShopDetails getDetails(ShopCreateDto dto) throws LocationNotFoundException {
        LocalizationStrategy strategy = dto.hasLocalization() ? new LocalizationDefaultStrategy() :
                new LocalizationApiStrategy(locationService);
        return new ShopDetails(dto.getStockType(), strategy.getLocalization(dto), getOpenHours(dto.getOpenHours()));
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
        Function<Shop, Shop> notify = s -> {
            if(s.getShopStats().getPeopleInside() > s.getShopStats().getMaxCapacity() * NOTIFIER_PEOPLE_MULTIPLIER) {
                notificationService.sendNotificationsToShopList(s);
            }
            notificationService.deleteExpiredNotifications();
            return s;
        };
        Function<Shop, ShopStatsDto> mapToDto = s -> new ShopStatsDto(s.getShopStats());
        var saveAndNotifyAndMap = save.andThen(notify).andThen(mapToDto);

        return Try.of(() -> shop.updatePeople(deltaInside, deltaQueue))
                  .andThen(this::publishShopStatsChangedEvent)
                  .toEither(Error.badRequest("CANNOT_UPDATE_STATS"))
                  .map(saveAndNotifyAndMap);
    }

    private void publishShopStatsChangedEvent(Shop shop) {
        var event = new ShopStatsChangedEvent(shop);

        eventPublisher.publishEvent(event);
    }


    public Shop updateShopDetails(Shop shop, ShopUpdateDto dto) {
        List<ShopCreateDto.OpenHours> dtoOpenHours = dto.getOpenHours();
        OpenHours newOpenHours = dtoOpenHours != null ? getOpenHours(dtoOpenHours) : null;

        shop.updateShopNameAndDetails(dto.getName(), dto.getStockType(), newOpenHours);
        return repository.save(shop);
    }

    Shop createShop(ShopCreateDto dto) throws LocationNotFoundException {
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

    Iterable<ShopOutputDto> findAll(ShopFilterCriteria criteria, Pageable pageable) {
        Specification<Shop> spec = criteria.toSpecification();
        var allShops = repository.findAll(spec, pageable)
                                 .stream()
                                 .peek(shop -> updateShopSearchStats(shop.getId(), criteria))
                                 .collect(Collectors.toUnmodifiableList());

        var distanceCriteria = criteria.getDistance();

        if (distanceCriteria == null) {
            return allShops.stream()
                           .map(ShopOutputDto::new)
                           .collect(Collectors.toList());
        }

        var inaccurateStrategy = new StraightLineDistance();
        var accurateStrategy = new LocationApiDistance(locationService, inaccurateStrategy);

        var closestShopsWithDistance = distanceService.getClosestShops(allShops, inaccurateStrategy, distanceCriteria);
        var closestShops = closestShopsWithDistance.stream()
                                                   .map(Tuple2::_1)
                                                   .collect(Collectors.toUnmodifiableList());

        var closestByApi = distanceService.getClosestShops(closestShops, accurateStrategy, distanceCriteria);

        return closestByApi.isEmpty() ? getOutputList(closestShopsWithDistance, inaccurateStrategy) :
                getOutputList(closestByApi, accurateStrategy);
    }

    private Iterable<ShopOutputDto> getOutputList(List<Tuple2<Shop, Double>> closestShops, DistanceCalculationStrategy strategy) {
        return closestShops.stream()
                           .map(t -> new ShopWithDistanceOutputDto(t._1, t._2, strategy.isAccurate()))
                           .collect(Collectors.toUnmodifiableList());
    }
}
