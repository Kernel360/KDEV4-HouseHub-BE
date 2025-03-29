package com.househub.backend.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuestionTypeValidator.class)
@Target({ElementType.FIELD}) // 어노테이션 필드에 적용 가능
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQuestionType {
    String message() default "유효하지 않은 질문 유형입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
