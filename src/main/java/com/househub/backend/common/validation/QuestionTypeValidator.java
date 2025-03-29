package com.househub.backend.common.validation;

import com.househub.backend.domain.inquiryForm.entity.QuestionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class QuestionTypeValidator implements ConstraintValidator<ValidQuestionType, QuestionType> {

    @Override
    public boolean isValid(QuestionType value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // null 값은 허용 X
        }
        for (QuestionType type : QuestionType.values()) {
            if (type.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
