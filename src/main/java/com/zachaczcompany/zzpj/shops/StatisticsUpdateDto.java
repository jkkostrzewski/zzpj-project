package com.zachaczcompany.zzpj.shops;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnegative;

@Data
@AllArgsConstructor
public
class StatisticsUpdateDto {
    @Nonnegative
    private int peopleJoinedQueue;

    @Nonnegative
    private int peopleWentInside;

    @Nonnegative
    private int peopleLeftQueue;

    @Nonnegative
    private int peopleLeftInside;
}
