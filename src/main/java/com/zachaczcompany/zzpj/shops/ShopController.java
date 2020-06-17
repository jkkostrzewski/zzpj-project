package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.ShopFacade;
import com.zachaczcompany.zzpj.shops.domain.ShopFilterCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all shops that meet filter requirements", description = "Requires criteria in path")
    @GetMapping("/shops")
    Iterable<ShopOutputDto> sortBy(@Valid ShopFilterCriteria criteria, Pageable pageable) {
        return shopFacade.findAll(criteria, pageable);
    }

    @Operation(summary = "Update shop statistics", description = "Requires shop id in param and update dto in body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful shop statistics update"),
            @ApiResponse(responseCode = "400", description = "Shop with that id does not exist")
    })
    @PutMapping("/shops/stats")
    ResponseEntity updateStatistics(@RequestParam @Nonnegative long id, @Valid @RequestBody StatisticsUpdateDto dto) {
        return shopFacade.updateShopStats(id, dto).toResponseEntity();
    }

    @Operation(summary = "Update shop details", description = "Requires shop id in param and update dto in body")
    @PutMapping("/shops")
    ResponseEntity updateShopDetails(@RequestParam @Nonnegative long id, @Valid @RequestBody ShopUpdateDto dto) {
        return shopFacade.updateShopDetails(id, dto).toResponseEntity();
    }

    @Operation(summary = "Get shop search history statistics for given shop id", description = "Requires shop id in param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful shop search history statistics update"),
            @ApiResponse(responseCode = "400", description = "Shop with that id does not exist")
    })
    @GetMapping("/shops/search/stats")
    ResponseEntity getShopSearchStatsById(@RequestParam @Nonnegative long searchId) {
        return shopFacade.findByShopSearchId(searchId).toResponseEntity();
    }
}
