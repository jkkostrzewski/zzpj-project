package com.zachaczcompany.zzpj.shops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ShopFacade {
    private final ShopRepository shopRepository;

    @Autowired
    public ShopFacade(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            Random random = new Random();

            List<Shop> shops = IntStream.range(0, 10).mapToObj(i -> new Shop("nazwa",
                    new Address("Łódź", "Ulica", i, "2b", "01-001"),
                    new ShopDetails(StockType.SERVICE,
                            new Localization(random.nextDouble(), random.nextDouble()),
                            new OpenHours(DailyOpenHours.always(LocalTime.NOON, LocalTime.MAX))),
                    new ShopStats(random.nextInt(100), random.nextInt(100), random.nextInt(100))
            )).collect(Collectors.toList());

            shopRepository.saveAll(shops);
        };
    }

    Optional<Shop> findShopById(Long id) {
        return shopRepository.findById(id);
    }

    public Iterable<Shop> findAll(ShopFilterCriteria criteria, Pageable pageable) {
        Specification<Shop> spec = criteria.toSpecification();
        return shopRepository.findAll(spec, pageable);
    }
}
