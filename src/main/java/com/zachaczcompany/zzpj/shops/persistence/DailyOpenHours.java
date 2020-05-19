package com.zachaczcompany.zzpj.shops.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lombok.AccessLevel.PACKAGE;

@Embeddable
@Getter
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor
public class DailyOpenHours {
    private DayOfWeek dayOfWeek;

    private LocalTime openFrom;

    private LocalTime openTo;

    public static Set<DailyOpenHours> always(LocalTime openFrom, LocalTime openTo) {
        return Stream.of(DayOfWeek.values()).map(day -> new DailyOpenHours(day, openFrom, openTo)).collect(Collectors.toSet());
    }
}