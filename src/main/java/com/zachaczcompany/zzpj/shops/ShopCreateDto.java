package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.Localization;
import com.zachaczcompany.zzpj.shops.domain.StockType;
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
    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @Nonnegative
    private int building;

    private String apartment;

    @Pattern(regexp = "\\d{2}-\\d{3}")
    private String zipCode;

    @NotNull
    private StockType stockType;

    @NotNull
    private Localization localization;

    @Size(min = 7, max = 7)
    private List<OpenHours> openHours;

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
}
