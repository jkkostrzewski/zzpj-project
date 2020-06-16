package com.zachaczcompany.zzpj.shops.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Getter
@NoArgsConstructor(access = PACKAGE)
public class OpenHours {
    @Setter(PRIVATE)
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "DAILY_OPEN_HOURS", joinColumns = @JoinColumn(name = "OPEN_HOURS_ID"))
    private Set<DailyOpenHours> openHours = Set.of();

    OpenHours(Set<DailyOpenHours> openHours) {
        checkIfContainsEveryDayOfWeek(openHours);
        this.openHours = openHours;
    }

    private void checkIfContainsEveryDayOfWeek(Set<DailyOpenHours> openHours) {
        if (!containsEveryDayOfWeek(openHours)) {
            throw new IllegalArgumentException("Every day of week must be covered");
        }
    }

    private boolean containsEveryDayOfWeek(Set<DailyOpenHours> openHours) {
        if (openHours.size() != 7) {
            return false;
        }

        List<DayOfWeek> days = new ArrayList<>(Arrays.asList(DayOfWeek.values()));
        openHours.forEach(daily -> days.remove(daily.getDayOfWeek()));
        return days.isEmpty();
    }
}
