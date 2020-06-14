package com.zachaczcompany.zzpj.shops;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class OpinionDto {
    private Long shopId;
    private Integer rate;
    private String description;
}
