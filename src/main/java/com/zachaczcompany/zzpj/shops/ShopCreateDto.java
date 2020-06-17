package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.Localization;
import com.zachaczcompany.zzpj.shops.domain.StockType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnegative;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static lombok.AccessLevel.PACKAGE;

@Getter
@Setter(PACKAGE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopCreateDto {
    @Schema(description = "Shop name", example = "Leader", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Name of city that shop is located in", example = "Warsaw", required = true)
    @NotBlank
    private String city;

    @Schema(description = "Street in which the shop is located in", example = "Yellow", required = true)
    @NotBlank
    private String street;

    @Schema(description = "Building number on street", example = "6", required = true)
    @Nonnegative
    private int building;

    @Schema(description = "Building apartment", example = "12")
    private String apartment;

    @Schema(description = "Street zip code", example = "02-123", required = true)
    @Pattern(regexp = "\\d{2}-\\d{3}")
    private String zipCode;

    @Schema(description = "Main type of stock that shop has", example = "DETERGENTS", required = true)
    @NotNull
    private StockType stockType;

    @Schema(description = "Geographic localization of shop")
    private Localization localization;

    @Schema(description = "Shop open hours")
    @Size(min = 7, max = 7)
    private List<OpenHours> openHours;

    @Schema(description = "Maximum shop capacity", example = "10")
    @Nonnegative
    private int maxCapacity;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class OpenHours {
        private DayOfWeek dayOfWeek;
        private LocalTime openFrom;
        private LocalTime openTo;
    }

    public boolean hasLocalization() {
        return localization != null;
    }
}
