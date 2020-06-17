package com.zachaczcompany.zzpj.shops.domain.validation.distance;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {DistanceCriteriaValidator.class})
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidDistanceCriteria {
    String message() default "Distance criteria must be null or completely filled!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
