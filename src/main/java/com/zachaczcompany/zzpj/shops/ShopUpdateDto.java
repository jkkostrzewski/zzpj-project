package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.domain.StockType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;


@Getter
@AllArgsConstructor
public class ShopUpdateDto {
    @Schema(description = "Shop name", example = "Leader", required = true)
    private String name;

    @Schema(description = "Main type of stock that shop has", example = "DETERGENTS", required = true)
    private StockType stockType;

    @Schema(description = "Shop open hours")
    @Size(min = 7, max = 7)
    private List<ShopCreateDto.OpenHours> openHours;
}
