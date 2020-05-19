package com.zachaczcompany.zzpj.shops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
class ShopController {
    private final ShopFacade shopFacade;

    @Autowired
    ShopController(ShopFacade shopFacade) {
        this.shopFacade = shopFacade;
    }

    @GetMapping("/shops")
    Iterable<ShopOutputDto> sortBy(ShopFilterCriteria criteria, Pageable pageable) {
        WebRequest request = null;
        Iterable<Shop> shops = shopFacade.findAll(criteria, pageable);

        return StreamSupport.stream(shops.spliterator(), false).map(ShopOutputDto::new).collect(Collectors.toList());
    }
}
