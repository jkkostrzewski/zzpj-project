package com.zachaczcompany.zzpj.commons;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeHelper {
    private final static Clock CLOCK = Clock.systemUTC();

    public static LocalDate date() {
        return LocalDate.now(CLOCK);
    }

    public static LocalTime time() {
        return LocalTime.now(CLOCK);
    }

    public static LocalDateTime dateTime() {
        return LocalDateTime.now(CLOCK);
    }
}
