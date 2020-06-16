package com.zachaczcompany.zzpj.history.shopStats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
@NoArgsConstructor
@AllArgsConstructor
class ShopStatsDiff {
    private long shopId;
    private Duration updatedAfter;
    private int queueDiff;
    private int insideDiff;

    static ShopStatsDiff of(ShopStatsMemento older, ShopStatsMemento younger) {
        if (older.isAfter(younger)) {
            var temp = older;
            older = younger;
            younger = temp;
        }

        return younger.diff(older);
    }

    @JsonProperty("updatedAfter")
    public long getUpdatedAfter() {
        return updatedAfter.toMillis();
    }
}


