package com.househub.backend.domain.customer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.common.response.ErrorResponse;

public class ExcelParserUtils {

	public static <T> ExcelParseResult<T> parseExcel(
		MultipartFile file,
		RowProcessor<T> rowProcessor
	) {

		List<T> successList = new ArrayList<>();
		List<ErrorResponse.FieldError> errors = new ArrayList<>();

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				try {
					T result = rowProcessor.process(row);
					if(result != null)
						successList.add(result);
				} catch (InvalidExcelValueException ex) {
					errors.addAll(ex.getFieldErrors());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("엑셀 처리 실패", e);
		}

		return new ExcelParseResult<>(successList, errors);
	}

	@FunctionalInterface
	public interface RowProcessor<T> {
		T process(Row row) throws InvalidExcelValueException;
	}

	public record ExcelParseResult<T>(List<T> successData, List<ErrorResponse.FieldError> errors) {}
}
