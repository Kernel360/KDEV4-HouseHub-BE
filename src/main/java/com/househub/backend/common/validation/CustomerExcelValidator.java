package com.househub.backend.common.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.househub.backend.common.response.ErrorResponse.FieldError;
import com.househub.backend.domain.customer.enums.CustomerExcelColumn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class CustomerExcelValidator implements ConstraintValidator<ValidCustomerExcel, Row> {

    @Override
    public void initialize(ValidCustomerExcel constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Row row, ConstraintValidatorContext context) {
        if (row == null) return true;

        List<FieldError> errors = validateRow(row);
        return errors.isEmpty();
    }

    private final Map<CustomerExcelColumn, CellValidator> validators = Map.of(
        CustomerExcelColumn.NAME, this::validateName,
        CustomerExcelColumn.AGE_GROUP, this::validateAgeGroup,
        CustomerExcelColumn.CONTACT, this::validateContact,
        CustomerExcelColumn.EMAIL, this::validateEmail,
        CustomerExcelColumn.MEMO, this::validateMemo,
        CustomerExcelColumn.GENDER, this::validateGender
    );

    public List<FieldError> validateRow(Row row) {
        List<FieldError> errors = new ArrayList<>();
        for (CustomerExcelColumn col : CustomerExcelColumn.values()) {
            Cell cell = row.getCell(col.ordinal());
            FieldError error = validators.get(col).validate(cell);
            if (error != null) errors.add(error);
        }
        return errors;
    }

    // 이름: 비어 있으면 통과, 값이 있으면 형식만 체크
    private FieldError validateName(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("name")
                .message("이름은 문자값만 입력 가능합니다.")
                .build();
        }
        String value = cell.getStringCellValue();
        if (!value.trim().isEmpty() && value.length() > 50) {
            return FieldError.builder()
                .field("name")
                .message("이름은 50자 이하만 가능합니다.")
                .build();
        }
        return null;
    }

    // 연령대: 비어 있으면 통과, 값이 있으면 형식만 체크
    private FieldError validateAgeGroup(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if (cell.getCellType() != CellType.NUMERIC){
            return FieldError.builder()
                .message("연령대는 숫자가 입력되어야 합니다.")
                .field("ageGroup")
                .build();
        }
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

    // 연락처: 반드시 값이 있어야 하고, 형식 체크
    private FieldError validateContact(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return FieldError.builder()
                .field("contact")
                .message("연락처는 필수 입력값입니다!")
                .build();
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("contact")
                .message("연락처는 문자값만 입력 가능합니다.")
                .build();
        }
        String value = cell.getStringCellValue();
        if (!value.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) {
            return FieldError.builder()
                .field("contact")
                .message("입력값 : " + value + " 이 양식에 맞지 않습니다. 000-0000-0000")
                .build();
        }
        return null;
    }

    // 이메일: 비어 있으면 통과, 값이 있으면 형식만 체크
    private FieldError validateEmail(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("name")
                .message("이메일은 문자값만 입력 가능합니다.")
                .build();
        }
        String value = cell.getStringCellValue();
        if (!value.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return FieldError.builder()
                .field("email")
                .message("입력값 : " + value + " 이 양식에 맞지 않습니다.")
                .build();
        }
        return null;
    }

    // 메모: 항상 통과
    private FieldError validateMemo(Cell cell) {
        return null;
    }

    // 성별: 비어 있으면 통과, 값이 있으면 형식만 체크
    private FieldError validateGender(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("name")
                .message("성별은 문자값만 입력 가능합니다.")
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
