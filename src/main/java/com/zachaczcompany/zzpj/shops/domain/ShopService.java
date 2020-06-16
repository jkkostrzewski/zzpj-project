package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.ZipCode;
import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.history.shopStats.ShopStatsChangedEvent;
import com.zachaczcompany.zzpj.location.integration.LocationRestService;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.ShopOutputDto;
import com.zachaczcompany.zzpj.shops.ShopStatsDto;
import com.zachaczcompany.zzpj.shops.ShopWithDistanceOutputDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import com.zachaczcompany.zzpj.shops.distance.DistanceCalculator;
import com.zachaczcompany.zzpj.shops.distance.LocationApiDistance;
import com.zachaczcompany.zzpj.shops.distance.StraightLineDistance;
import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import com.zachaczcompany.zzpj.shops.exceptions.LocationNotFoundException;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class ShopService {
    private final ApplicationEventPublisher eventPublisher;
    private final ShopSearchRepository searchRepository;
    private final LocationRestService locationService;
    private final ShopRepository repository;
    private final int limitOfRequests;
    private final double toleranceRatio;

    @Autowired
    ShopService(ApplicationEventPublisher eventPublisher, ShopRepository repository,
                ShopSearchRepository shopSearchRepository, LocationRestService locationService,
                @Value("${locationiq.rest-api.numberOfRequests}") int limitOfRequests,
                @Value("${locationiq.rest-api.toleranceRatio}") double toleranceRatio) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
        this.searchRepository = shopSearchRepository;
        this.locationService = locationService;
        this.limitOfRequests = limitOfRequests;
        this.toleranceRatio = toleranceRatio;
    }

    private static Address getAddress(ShopCreateDto dto) {
        return new Address(dto.getCity(), dto.getStreet(), dto.getBuilding(),
                Objects.toString(dto.getApartment(), ""),
                new ZipCode(dto.getZipCode()));
    }

    private ShopDetails getDetails(ShopCreateDto dto) throws LocationNotFoundException {
        LocalizationStrategy strategy = dto.hasLocalization() ? new LocalizationDefaultStrategy() :
                new LocalizationApiStrategy(locationService);
        return new ShopDetails(dto.getStockType(), strategy.getLocalization(dto), getOpenHours(dto));
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
        var inaccurateStrategy = new StraightLineDistance();
        var accurateStrategy = new LocationApiDistance(locationService, inaccurateStrategy);
        var distanceCalculator = new DistanceCalculator(inaccurateStrategy, accurateStrategy);

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

        var closestShops = getClosestShops(allShops, distanceCalculator, distanceCriteria);
        var closestByApi = getClosestShopsUsingApi(closestShops, distanceCalculator, distanceCriteria);

        return closestByApi.isEmpty() ? getOutputList(closestShops, false) : getOutputList(closestByApi, true);
    }

    private Iterable<ShopOutputDto> getOutputList(List<Tuple2<Shop, Double>> closestShops, boolean accurate) {
        return closestShops.stream()
                           .map(t -> new ShopWithDistanceOutputDto(t._1, t._2, accurate))
                           .collect(Collectors.toList());
    }

    private List<Tuple2<Shop, Double>> getClosestShops(List<Shop> shops, DistanceCalculator distanceCalculator, DistanceCriteria distanceCriteria) {
        return shops.stream()
                    .map(shop -> toTupleWithInaccurateDistance(shop, distanceCriteria, distanceCalculator))
                    .filter(t -> isCloseEnough(t, distanceCriteria))
                    .sorted(Comparator.comparing(t -> t._2))
                    .limit(this.limitOfRequests)
                    .collect(Collectors.toUnmodifiableList());
    }

    private List<Tuple2<Shop, Double>> getClosestShopsUsingApi(List<Tuple2<Shop, Double>> closestShops, DistanceCalculator distanceCalculator, DistanceCriteria distanceCriteria) {
        return closestShops.stream()
                           .map(Tuple2::_1)
                           .map(s -> toTupleWithAccurateDistance(s, distanceCriteria, distanceCalculator))
                           .sorted(Comparator.comparing(t -> t._2))
                           .collect(Collectors.toUnmodifiableList());
    }

    private boolean isCloseEnough(Tuple2<Shop, Double> tuple, DistanceCriteria criteria) {
        return tuple._2 <= toleranceRatio * criteria.getRadius();
    }

    private Tuple2<Shop, Double> toTupleWithInaccurateDistance(Shop shop, DistanceCriteria distanceCriteria, DistanceCalculator calculator) {
        return toTupleWithDistance(shop, distanceCriteria, calculator::inaccurateDistance);
    }

    private Tuple2<Shop, Double> toTupleWithAccurateDistance(Shop shop, DistanceCriteria distanceCriteria, DistanceCalculator calculator) {
        return toTupleWithDistance(shop, distanceCriteria, calculator::accurateDistance);
    }

    private Tuple2<Shop, Double> toTupleWithDistance(Shop shop, DistanceCriteria criteria, BiFunction<Localization, Localization, Double> calculator) {
        var from = criteria.getLocalization();
        var to = shop.getLocalization();

        double calculated = calculator.apply(from, to);
        return Tuple.of(shop, calculated);
    }
}
