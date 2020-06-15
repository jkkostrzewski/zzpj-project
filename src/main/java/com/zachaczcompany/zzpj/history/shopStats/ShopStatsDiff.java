package com.zachaczcompany.zzpj.history.shopStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

import static lombok.AccessLevel.PACKAGE;

@Getter
@Setter(PACKAGE)
@NoArgsConstructor
@AllArgsConstructor
class ShopStatsDiff {
    private long shopId;
    private Duration updatedAfter;
    private int queueDiff;
    private int insideDiff;

    static ShopStatsDiff of(ShopStatsMemento older, ShopStatsMemento younger){
        if (older.isAfter(younger)){
            var temp = older;
            older = younger;
            younger = temp;
        }

        return older.diff(younger);
    }
}


