package com.zachaczcompany.zzpj.shops.domain;

class OpenHoursValidationRule {
    boolean isValid(DailyOpenHours dailyOpenHours) {
        return dailyOpenHours.getOpenFrom().isBefore(dailyOpenHours.getOpenTo());
    }
}
