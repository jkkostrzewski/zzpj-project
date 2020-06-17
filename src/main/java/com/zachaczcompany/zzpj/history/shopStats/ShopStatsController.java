package com.zachaczcompany.zzpj.history.shopStats;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get statistics history for a shop", description = "Requires shop id in param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful shop statistics get"),
            @ApiResponse(responseCode = "400", description = "Shop with that id does not exist")
    })
    @GetMapping
    ResponseEntity getDiffs(@RequestParam long shopId) {
        return service.getDiffsForShop(shopId).toResponseEntity();
    }
}
