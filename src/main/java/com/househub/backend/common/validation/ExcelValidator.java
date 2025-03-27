package com.househub.backend.common.validation;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.common.response.ErrorResponse.FieldError;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public class ExcelValidator implements ConstraintValidator<ValidExcel, Row> {

    @Override
    public void initialize(ValidExcel constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Row row, ConstraintValidatorContext context) {
        if (row == null) return true;

        List<FieldError> errors = validateRow(row);
        return errors.isEmpty();
    }

    public List<FieldError> validateRow(Row row) {
        List<FieldError> errors = new ArrayList<>();
        validateCell(row.getCell(0), this::validateName,errors);
        validateCell(row.getCell(1), this::validateAgeGroup, errors);
        validateCell(row.getCell(2), this::validateContact, errors);
        validateCell(row.getCell(3), this::validateEmail, errors);
        validateCell(row.getCell(4), this::validateMemo, errors);
        validateCell(row.getCell(5), this::validateGender, errors);

        return errors;
    }

    private void validateCell(Cell cell, CellValidator validator, List<FieldError> errors) {
        if (cell != null) {
            FieldError error = validator.validate(cell);
            if (error != null) {
                errors.add(error);
            }
        }
    }

    private FieldError validateName(Cell cell) {
        String value = cell.getStringCellValue();
        if (value.trim().isEmpty()) {
            return FieldError.builder()
                    .field("name")
                    .message("이름은 비어있을 수 없습니다.")
                    .build();
        }
        return null;
    }

    private FieldError validateAgeGroup(Cell cell) {
        try {
            int age;
            if (cell.getCellType() == CellType.NUMERIC) {
                age = (int) cell.getNumericCellValue();
            } else {
                age = Integer.parseInt(cell.getStringCellValue());
            }
            if (age <= 0 || age >= 100 || age % 10 != 0) {
                return FieldError.builder()
                        .field("ageGroup")
                        .message(age + ": 0 초과 100 미만의 10의 배수여야 합니다.")
                        .build();
            }
        } catch (NumberFormatException | IllegalStateException e) {
            return FieldError.builder()
                    .field("ageGroup")
                    .message("입력값이 유효한 숫자가 아닙니다.")
                    .build();
        }
        return null;
    }


    private FieldError validateContact(Cell cell) {
        String value = cell.getStringCellValue();
        if (!value.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) {
            return FieldError.builder()
                    .field("contact")
                    .message("입력값 : " + value + " 이 양식에 맞지 않습니다. 000-0000-0000")
                    .build();
        }
        return null;
    }

    private FieldError validateEmail(Cell cell) {
        String value = cell.getStringCellValue();
        if (!value.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return FieldError.builder()
                    .field("email")
                    .message("입력값 : " + value + " 이 양식에 맞지 않습니다.")
                    .build();
        }
        return null;
    }

    private FieldError validateMemo(Cell cell) {
        return null;
    }

    private FieldError validateGender(Cell cell) {
        if (cell == null) {
            return FieldError.builder()
                    .field("gender")
                    .message("성별 값이 비어있습니다.")
                    .build();
        }

        String value = cell.getStringCellValue().trim().toUpperCase();
        if (!value.equals("M") && !value.equals("F")) {
            return FieldError.builder()
                    .field("gender")
                    .message("입력값 : " + value + " 이 양식에 맞지 않습니다. (M 또는 F만 허용)")
                    .build();
        }
        return null;
    }


    @FunctionalInterface
    private interface CellValidator {
        FieldError validate(Cell cell);
    }
}
