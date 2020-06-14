package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.shops.ShopStatsDto;
import com.zachaczcompany.zzpj.shops.StatisticsUpdateDto;
import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.function.Function;

@Service
class ShopService {
    private final ShopRepository repository;
    private final ShopSearchRepository shopSearchRepository;  //TODO usunąć razem z clr()

    @Autowired
    public ShopService(ShopRepository repository, ShopSearchRepository shopSearchRepository) {
        this.repository = repository;
        this.shopSearchRepository = shopSearchRepository;
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

    //TODO usunac po dodaniu tworzenia sklepu przy rejestracji kierownika sklepu
    //TODO dodać tworzenie encji z ShopSearch przy tworzeniu sklepu
    //TODO usunąć referencje do shopSearchRepository na gorze
    @Bean
    CommandLineRunner clr() {
        return args -> {
            var address = new Address("miasto", "ulica", 1, "1a", "00-000");
            var localization = new Localization(0, 0);
            var openHours = new OpenHours(DailyOpenHours.always(LocalTime.MIDNIGHT, LocalTime.MIDNIGHT));
            var details = new ShopDetails(StockType.DETERGENTS, localization, openHours);
            var stats = new ShopStats(100, 0, 0);
            var shop1 = new Shop("Shop1", address, details, stats);
            repository.save(shop1);
            shopSearchRepository.save(new ShopSearch(shop1.getId()));
        };
    }
}
