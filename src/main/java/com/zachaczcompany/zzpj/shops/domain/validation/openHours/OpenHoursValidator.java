package com.zachaczcompany.zzpj.shops.domain.validation.openHours;

import com.zachaczcompany.zzpj.shops.domain.DailyOpenHours;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class OpenHoursValidator implements ConstraintValidator<ValidOpenHours, DailyOpenHours> {
    private final OpenHoursValidationRule validationRule = new OpenHoursValidationRule();

    @Override
    public boolean isValid(DailyOpenHours openHours, ConstraintValidatorContext constraintValidatorContext) {
        return validationRule.isValid(openHours);
    }
}
