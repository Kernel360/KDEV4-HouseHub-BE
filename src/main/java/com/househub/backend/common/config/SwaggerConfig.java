package com.househub.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI houseHubOpenAPI() {
		// 파일 업로드를 위한 스키마 생성
		Schema<?> fileSchema = new Schema<>()
			.type("string")
			.format("binary");

		// MediaType 객체 생성
		MediaType mediaType = new MediaType()
			.schema(fileSchema);

		// RequestBody 객체 생성
		RequestBody requestBody = new RequestBody()
			.content(new Content().addMediaType("multipart/form-data", mediaType))
			.required(true);

		// 경로 및 작업 설정
		Paths paths = new Paths();
		PathItem pathItem = new PathItem().post(
			new Operation()
				.summary("Excel 파일 업로드")
				.description("고객 정보가 담긴 Excel 파일을 업로드합니다.")
				.operationId("uploadExcelFile")
				.requestBody(requestBody)
		);

		paths.addPathItem("/api/customers/upload", pathItem);

		return new OpenAPI()
			.info(new Info()
				.title("HouseHub API 문서")
				.version("1.0.0")
				.description("HouseHub는 **공인중개사를 위한 고객 관리 서비스**입니다.\n\n" +
					"**주요 기능:**\n" +
					"- 고객 관리 (고객 정보 등록 및 조회)\n" +
					"- 매물 관리 (매물 등록 및 상태 변경)\n" +
					"- 상담 내역 관리 (상담 기록 및 일정 관리)\n" +
					"- 문의 내역 관리 (고객 문의 내역 저장 및 응답)\n" +
					"- 문자 관리 (고객 대상 문자 전송 및 템플릿 활용)\n" +
					"- 문의 템플릿 관리 (자주 사용하는 메시지 템플릿 등록 및 사용)\n\n" +
					"본 문서는 HouseHub의 백엔드 API에 대한 상세 설명을 제공합니다.")
			)
			.paths(paths);
	}
}
