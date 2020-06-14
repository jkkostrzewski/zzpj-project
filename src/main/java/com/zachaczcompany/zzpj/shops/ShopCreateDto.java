package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.DailyOpenHours;
import com.zachaczcompany.zzpj.shops.domain.Localization;
import com.zachaczcompany.zzpj.shops.domain.StockType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnegative;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static lombok.AccessLevel.PACKAGE;

@Getter
@Setter(PACKAGE)
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

    @NotNull
    private List<DailyOpenHours> openHours;

    @Nonnegative
    private int maxCapacity;
}
