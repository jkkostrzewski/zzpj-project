package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.ShopStats;
import io.swagger.v3.oas.annotations.media.Schema;

public class ShopStatsDto {
    @Schema(description = "Shop id number", example = "5", required = true)
    public final long id;
    @Schema(description = "Number of people inside of the shop", example = "12", required = true)
    public final int peopleInside;
    @Schema(description = "Number of people in queue", example = "3", required = true)
    public final int peopleInQueue;

    public ShopStatsDto(ShopStats stats) {
        id = stats.getId();
        peopleInQueue = stats.getPeopleInQueue();
        peopleInside = stats.getPeopleInside();
    }
}
