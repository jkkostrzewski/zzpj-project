package com.zachaczcompany.zzpj.shops;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@Getter
public class OpinionDto {
    private Long shopId;
    @Min(0)
    @Max(5)
    private Integer rate;
    private String description;
}
