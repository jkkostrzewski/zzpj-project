package com.zachaczcompany.zzpj.shops.persistence;

import lombok.AccessLevel;
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

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenHours {
    @Setter(AccessLevel.PRIVATE)
    @ElementCollection
    @CollectionTable(name = "DAILY_OPEN_HOURS", joinColumns = @JoinColumn(name = "OPEN_HOURS_ID"))
    private Set<DailyOpenHours> openHours = Set.of();

    public OpenHours(Set<DailyOpenHours> openHours) {
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
