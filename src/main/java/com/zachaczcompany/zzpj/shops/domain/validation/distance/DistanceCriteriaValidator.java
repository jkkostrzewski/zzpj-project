package com.zachaczcompany.zzpj.shops.domain.validation.distance;

import com.zachaczcompany.zzpj.shops.domain.DistanceCriteria;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class DistanceCriteriaValidator implements ConstraintValidator<ValidDistanceCriteria, DistanceCriteria> {
    @Override
    public boolean isValid(DistanceCriteria distanceCriteria, ConstraintValidatorContext constraintValidatorContext) {
        return distanceCriteria == null ^ (distanceCriteria != null && noNullField(distanceCriteria));
    }

    private boolean noNullField(DistanceCriteria distanceCriteria) {
        return distanceCriteria.getLatitude() != null
                && distanceCriteria.getLongitude() != null
                && distanceCriteria.getRadius() != null;
    }
}
