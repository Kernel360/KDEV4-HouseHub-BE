package com.househub.backend.common.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthDateAdultValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {
	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if (value == null) return true; // 비어 있으면 통과
		LocalDate now = LocalDate.now();
		if (value.isAfter(now)) return false;
		return !value.plusYears(19).isAfter(now);
	}
}
