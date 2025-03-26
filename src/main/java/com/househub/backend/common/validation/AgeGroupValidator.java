package com.househub.backend.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeGroupValidator implements ConstraintValidator<ValidAgeGroup, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null){
            return false;
        }
        return value >= 0 && value <= 100 && value % 10 == 0;
    }
}
