package com.househub.backend.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeGroupValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAgeGroup {
    String message() default "연령대는 10의 배수여야 합니다 (0, 10, 20, 30, ...)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
