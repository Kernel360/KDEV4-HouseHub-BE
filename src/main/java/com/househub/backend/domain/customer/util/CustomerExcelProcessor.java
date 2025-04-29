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
import com.househub.backend.common.validation.CustomerExcelValidator;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.enums.CustomerExcelColumn;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerExcelProcessor {

	private final CustomerExcelValidator customerExcelValidator;

	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

	private static void validateHeader(Row headerRow) {
		for (int i = 0; i < CustomerExcelColumn.size(); i++) {
			Cell cell = headerRow.getCell(i);
			if (cell == null || !CustomerExcelColumn.fromIndex(i).getHeader().equals(cell.getStringCellValue())) {
				throw new InvalidFormatException("헤더가 정해진 형식과 일치하지 않습니다.","HEADER_INVALID");
			}
		}
	}

	private void validateFileExtension(String extension) {
		if (!"xlsx".equalsIgnoreCase(extension) && !"xls".equalsIgnoreCase(extension)) {
			throw new InvalidFormatException("엑셀 파일(xlsx,xls)만 업로드 가능합니다.", "EXTENSION_ERROR");
		}
	}

	private void validateFileSize(long size) {
		if (size > MAX_FILE_SIZE) {
			throw new InvalidFormatException(
				"파일 크기는" + (MAX_FILE_SIZE/1024/1024) + "MB를 초과할 수 없습니다.",
				"FILE_SIZE_EXCEEDED"
			);
		}
	}

	public ExcelParserUtils.ExcelParseResult<CreateCustomerReqDto> process(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(fileName);

		validateFileExtension(extension);
		validateFileSize(file.getSize());

		return ExcelParserUtils.parseExcel(file, row -> {
			if (row.getRowNum() == 0) {
				validateHeader(row);
				return null;
			}

			boolean allEmpty = true;
			for (Cell cell : row) {
				if(!isCellEmpty(cell)) {
					allEmpty = false;
					break;
				}
			}
			if (allEmpty) return null;

			List<FieldError> errors = customerExcelValidator.validateRow(row);
			if (!errors.isEmpty()) {
				throw new InvalidExcelValueException("입력값 오류", errors, "VALIDATION_ERROR");
			}

			return buildCustomerDto(row);
		});
	}

	// cell이 null이거나, cell의 값이 빈 문자열(공백 포함)인지 확인
	private boolean isCellEmpty(Cell cell) {
		if(cell == null || cell.getCellType() == CellType.BLANK) return true;
		if(cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) return true;
		return false;
	}

	// 문자열 셀 값 가져오기
	private String getStringCell(Row row, CustomerExcelColumn col) {
		Cell cell = row.getCell(col.ordinal());
		return cell != null ? emptyToNull(cell.getStringCellValue()) : null;
	}

	// 생년월일 셀 값 가져오기
	private java.time.LocalDate getBirthDateCell(Row row, CustomerExcelColumn col) {
		Cell cell = row.getCell(col.ordinal());
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return null;
		}
		String value = cell.getStringCellValue();
		return value != null && !value.trim().isEmpty() ? java.time.LocalDate.parse(value) : null;
	}

	private String emptyToNull(String value) {
		return (value == null || value.trim().isEmpty()) ? null : value;
	}

	private CreateCustomerReqDto buildCustomerDto(Row row) {
		return CreateCustomerReqDto.builder()
			.name(getStringCell(row, CustomerExcelColumn.NAME))
			.birthDate(getBirthDateCell(row, CustomerExcelColumn.BIRTH_DATE))
			.contact(getStringCell(row, CustomerExcelColumn.CONTACT))
			.email(getStringCell(row, CustomerExcelColumn.EMAIL))
			.memo(getStringCell(row, CustomerExcelColumn.MEMO))
			.gender(Gender.fromString(getStringCell(row, CustomerExcelColumn.GENDER)))
			.build();
	}

}