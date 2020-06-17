package com.zachaczcompany.zzpj.shops.domain.validation.openHours;

import com.zachaczcompany.zzpj.shops.domain.DailyOpenHours;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

class OpenHoursListValidator implements ConstraintValidator<ValidOpenHours, Collection<DailyOpenHours>> {
    private final OpenHoursValidationRule validationRule = new OpenHoursValidationRule();

    @Override
    public boolean isValid(Collection<DailyOpenHours> dailyOpenHours, ConstraintValidatorContext constraintValidatorContext) {
        return dailyOpenHours.stream().allMatch(validationRule::isValid);
    }
}
