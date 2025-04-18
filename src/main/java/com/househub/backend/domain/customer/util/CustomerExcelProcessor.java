package com.househub.backend.domain.customer.util;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.common.exception.InvalidFormatException;
import com.househub.backend.common.response.ErrorResponse.FieldError;
import com.househub.backend.common.validation.ExcelValidator;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerExcelProcessor {
	private final ExcelValidator excelValidator;

	public static void validateHeader(Row headerRow) {
		String[] expectedHeaders = {"name", "ageGroup", "contact", "email", "memo", "gender"};

		for (int i = 0; i < expectedHeaders.length; i++) {
			Cell cell = headerRow.getCell(i);
			if (cell == null || !expectedHeaders[i].equals(cell.getStringCellValue())) {
				throw new InvalidFormatException("헤더가 정해진 형식과 일치하지 않습니다.","HEADER_INVALID");
			}
		}
	}

	public ExcelParserUtils.ExcelParseResult<CreateCustomerReqDto> process(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(fileName);
		final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

		if (!"xlsx".equalsIgnoreCase(extension) && !"xls".equalsIgnoreCase(extension)) {
			throw new InvalidFormatException("엑셀 파일(xlsx,xls)만 업로드 가능합니다.", "EXTENSION_ERROR");
		}
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new InvalidFormatException(
				"파일 크기는" + (MAX_FILE_SIZE/1024/1024) + "MB를 초과할 수 없습니다.",
				"FILE_SIZE_EXCEEDED"
			);
		}
		return ExcelParserUtils.parseExcel(file, row -> {
			if (row.getRowNum() == 0) {
				validateHeader(row);
				return null;
			}
			boolean allEmpty = true;
			for (Cell cell : row) {
				// cell이 null이거나, cell의 값이 빈 문자열(공백 포함)인지 확인
				if (cell != null && cell.getCellType() != CellType.BLANK &&
					!(cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())) {
					allEmpty = false;
					break;
				}
			}
			if (allEmpty) {
				// 모든 cell이 비어있으면 해당 row는 건너뜀
				return null; // 또는 continue; (람다에서는 return null로 처리)
			}
			List<FieldError> errors = excelValidator.validateRow(row);
			if (!errors.isEmpty()) {
				throw new InvalidExcelValueException("입력값 오류", errors, "VALIDATION_ERROR");
			}
			return buildCustomerDto(row);
		});
	}

	private String emptyToNull(String value) {
		return (value == null || value.trim().isEmpty()) ? null : value;
	}

	private CreateCustomerReqDto buildCustomerDto(Row row) {
		return CreateCustomerReqDto.builder()
			.name(emptyToNull(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null))
			.ageGroup(row.getCell(1) != null ? (int)row.getCell(1).getNumericCellValue() : null)
			.contact(emptyToNull(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null))
			.email(emptyToNull(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null))
			.memo(emptyToNull(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null))
			.gender(Gender.fromString(
				row.getCell(5) != null ? row.getCell(5).getStringCellValue() : null
			))
			.build();
	}
}