package com.zachaczcompany.zzpj.history.shopStats;

import com.zachaczcompany.zzpj.shops.domain.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop/stats/history")
class ShopStatsController {
    //TODO paluszkiewiczB usunac w pizdu
    private final ShopStatsMementoRepository repository;
    private final ShopRepository shopRepository;

    private final ShopStatsMementoService service;

    @Autowired
    ShopStatsController(ShopStatsMementoRepository repository, ShopRepository shopRepository, ShopStatsMementoService service) {
        this.repository = repository;
        this.shopRepository = shopRepository;
        this.service = service;
    }

//    @GetMapping
//    String saveStuff() {
//        var shop = new Shop("nazwa", new Address("a", "a", 1, "a", new ZipCode("11-111")), null, new ShopStats(100, 0, 0));
//        shop = shopRepository.save(shop);
//        var memento = new ShopStatsMemento(shop);
//        return repository.save(memento).toString();
//    }

    @GetMapping
    ResponseEntity getDiffs(@RequestParam long shopId) {
        return service.getDiffsForShop(shopId).toResponseEntity();
    }
}
