package com.househub.backend.common.validation;

import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.househub.backend.common.response.ErrorResponse.FieldError;
import com.househub.backend.domain.customer.enums.CustomerExcelColumn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        CustomerExcelColumn.BIRTH_DATE, this::validateBirthDate,
        CustomerExcelColumn.CONTACT, this::validateContact,
        CustomerExcelColumn.EMAIL, this::validateEmail,
        CustomerExcelColumn.MEMO, this::validateMemo,
        CustomerExcelColumn.GENDER, this::validateGender
    );

    public List<FieldError> validateRow(Row row) {
        List<FieldError> errors = new ArrayList<>();
        for (CustomerExcelColumn col : CustomerExcelColumn.values()) {
            Cell cell = row.getCell(col.ordinal());
            FieldError error = validators.get(col).validate(cell, row.getRowNum() + 1);
            if (error != null) errors.add(error);
        }
        return errors;
    }

    // 이름: 비어 있으면 통과, 값이 있으면 형식만 체크
    private FieldError validateName(Cell cell, int rowNum) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("name")
                .message(String.format("%d행: 이름은 문자값만 입력 가능합니다.", rowNum))
                .build();
        }
        String value = cell.getStringCellValue();
        if (!value.trim().isEmpty() && value.length() > 50) {
            return FieldError.builder()
                .field("name")
                .message(String.format("%d행: 이름은 50자 이하만 가능합니다.", rowNum))
                .build();
        }
        return null;
    }

    // 생년월일: 비어 있으면 통과, 값이 있으면 형식과 성인 여부 체크
    private FieldError validateBirthDate(Cell cell, int rowNum) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        LocalDate birthDate = null;
        String value = null;

        try {
            switch (cell.getCellType()) {
                case STRING:
                    value = cell.getStringCellValue().trim();
                    if (!value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                        return formatError(String.format("%d행: 생년월일 형식 오류 (yyyy-MM-dd)", rowNum));
                    }
                    birthDate = LocalDate.parse(value);
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        birthDate = cell.getLocalDateTimeCellValue().toLocalDate();
                        value = birthDate.toString(); // yyyy-MM-dd 변환
                    } else {
                        log.warn("NUMERIC 타입이지만 날짜 형식 아님: {}", cell.getNumericCellValue());
                        return formatError(String.format("%d행: 날짜 형식으로 입력해주세요", rowNum));
                    }
                    break;
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                birthDate = cell.getLocalDateTimeCellValue().toLocalDate();
                                value = birthDate.toString();
                            }
                            break;
                        default:
                            return formatError(String.format("%d행: 수식 결과 타입 오류", rowNum));
                    }
                    break;
                default:
                    return formatError(String.format("%d행: 지원하지 않는 셀 타입", rowNum));
            }

            LocalDate now = LocalDate.now();
            if (birthDate == null && value != null) {
                birthDate = LocalDate.parse(value);
            }
            if (birthDate.isAfter(now)) {
                return formatError(String.format("%d행: 미래 날짜 입력 불가", rowNum));
            }
            if (birthDate.plusYears(19).isAfter(now)) {
                return formatError(String.format("%d행: 만 19세 미만 가입 불가", rowNum));
            }

        } catch (DateTimeParseException e) {
            log.error("날짜 파싱 실패: {}", e.getMessage());
            return formatError(String.format("%d행: 유효하지 않은 날짜", rowNum));
        } catch (Exception e) {
            log.error("예상치 못한 오류: {}", e.getMessage());
            return formatError(String.format("%d행: 시스템 오류 발생", rowNum));
        }

        return null;
    }

    // formatError도 rowNum을 받을 수 있게 수정
    private FieldError formatError(String message) {
        return FieldError.builder()
            .field("birthDate")
            .message(message)
            .build();
    }


    // 연락처: 반드시 값이 있어야 하고, 형식 체크
    private FieldError validateContact(Cell cell, int rowNum) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return FieldError.builder()
                .field("contact")
                .message(String.format("%d행: 연락처는 필수 입력값입니다!", rowNum))
                .build();
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("contact")
                .message(String.format("%d행: 연락처는 문자값만 입력 가능합니다.", rowNum))
                .build();
        }
        String value = cell.getStringCellValue();
        if (!value.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) {
            return FieldError.builder()
                .field("contact")
                .message(String.format("%d행: 입력값 '%s'이 양식에 맞지 않습니다. 000-0000-0000", rowNum, value))
                .build();
        }
        return null;
    }

    // 이메일: 비어 있으면 통과, 값이 있으면 형식만 체크
    private FieldError validateEmail(Cell cell, int rowNum) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("email")
                .message(String.format("%d행: 이메일은 문자값만 입력 가능합니다.", rowNum))
                .build();
        }
        String value = cell.getStringCellValue();
        if (!value.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return FieldError.builder()
                .field("email")
                .message(String.format("%d행: 입력값 '%s'이 양식에 맞지 않습니다.", rowNum, value))
                .build();
        }
        return null;
    }

    // 메모: 항상 통과
    private FieldError validateMemo(Cell cell, int rowNum) {
        return null;
    }

    // 성별: 비어 있으면 통과, 값이 있으면 형식만 체크
    private FieldError validateGender(Cell cell, int rowNum) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if(cell.getCellType()!=CellType.STRING){
            return FieldError.builder()
                .field("gender")
                .message(String.format("%d행: 성별은 문자값만 입력 가능합니다.", rowNum))
                .build();
        }
        String value = cell.getStringCellValue().trim().toUpperCase();
        if (!value.equals("M") && !value.equals("F")) {
            return FieldError.builder()
                .field("gender")
                .message(String.format("%d행: 입력값 '%s'이 양식에 맞지 않습니다. (M 또는 F만 허용)", rowNum, value))
                .build();
        }
        return null;
    }

    @FunctionalInterface
    private interface CellValidator {
        FieldError validate(Cell cell, int rowNum);
    }
}
