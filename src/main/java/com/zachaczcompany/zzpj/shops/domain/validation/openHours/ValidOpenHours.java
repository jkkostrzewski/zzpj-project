package com.zachaczcompany.zzpj.shops.domain.validation.openHours;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {OpenHoursValidator.class, OpenHoursListValidator.class})
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOpenHours {
    String message() default "Open hours cannot end before beginning!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
