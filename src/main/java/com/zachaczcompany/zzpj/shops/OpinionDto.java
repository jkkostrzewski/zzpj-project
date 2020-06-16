package com.zachaczcompany.zzpj.shops;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@Getter
public class OpinionDto {
    @Schema(description = "Shop id", example = "1", required = true)
    private Long shopId;
    @Schema(description = "Shop rating", example = "1", required = true)
    @Min(0)
    @Max(5)
    private Integer rate;
    @Schema(description = "Rating description. You can leave your written opinion here", example = "Nice shop", required = true)
    private String description;
}
