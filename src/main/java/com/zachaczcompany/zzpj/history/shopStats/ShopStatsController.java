package com.zachaczcompany.zzpj.history.shopStats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shops/stats/history")
class ShopStatsController {
    private final ShopStatsMementoService service;

    @Autowired
    ShopStatsController(ShopStatsMementoService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity getDiffs(@RequestParam long shopId) {
        return service.getDiffsForShop(shopId).toResponseEntity();
    }
}
