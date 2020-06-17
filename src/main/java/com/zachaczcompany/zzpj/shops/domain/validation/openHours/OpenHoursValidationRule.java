package com.zachaczcompany.zzpj.shops.domain.validation.openHours;

import com.zachaczcompany.zzpj.shops.domain.DailyOpenHours;

class OpenHoursValidationRule {
    boolean isValid(DailyOpenHours dailyOpenHours) {
        return dailyOpenHours.getOpenFrom().isBefore(dailyOpenHours.getOpenTo());
    }
}
