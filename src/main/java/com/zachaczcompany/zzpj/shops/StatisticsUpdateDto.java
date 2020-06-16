package com.zachaczcompany.zzpj.shops;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnegative;

@Data
@AllArgsConstructor
public
class StatisticsUpdateDto {
    @Schema(description = "Number of people that entered queue recently", example = "5")
    @Nonnegative
    private int peopleJoinedQueue;

    @Schema(description = "Number of people that entered shop recently", example = "5")
    @Nonnegative
    private int peopleEnteredShop;

    @Schema(description = "Number of people that left queue recently", example = "5")
    @Nonnegative
    private int peopleLeftQueue;

    @Schema(description = "Number of people that left shop recently", example = "5")
    @Nonnegative
    private int peopleLeftShop;
}
