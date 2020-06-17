package com.zachaczcompany.zzpj.shops.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class OpenHoursValidator implements ConstraintValidator<ValidOpenHours, DailyOpenHours> {
    private final OpenHoursValidationRule validationRule = new OpenHoursValidationRule();

    @Override
    public boolean isValid(DailyOpenHours openHours, ConstraintValidatorContext constraintValidatorContext) {
        return validationRule.isValid(openHours);
    }
}
