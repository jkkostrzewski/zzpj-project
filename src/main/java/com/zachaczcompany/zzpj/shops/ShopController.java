package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.ShopFacade;
import com.zachaczcompany.zzpj.shops.domain.ShopFilterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnegative;
import javax.validation.Valid;

@RestController
class ShopController {
    private final ShopFacade shopFacade;

    @Autowired
    ShopController(ShopFacade shopFacade) {
        this.shopFacade = shopFacade;
    }

    @GetMapping("/shops")
    Iterable<ShopOutputDto> sortBy(@Valid ShopFilterCriteria criteria, Pageable pageable) {
        return shopFacade.findAll(criteria, pageable);
    }

    @PutMapping("/shops/stats")
    ResponseEntity updateStatistics(@RequestParam @Nonnegative long id, @Valid @RequestBody StatisticsUpdateDto dto) {
        return shopFacade.updateShopStats(id, dto).toResponseEntity();
    }

    @GetMapping("/shops/search/stats")
    ResponseEntity getShopSearchStatsById(@RequestParam @Nonnegative long shopId) {
        return shopFacade.findByShopId(shopId).toResponseEntity();
    }
}
