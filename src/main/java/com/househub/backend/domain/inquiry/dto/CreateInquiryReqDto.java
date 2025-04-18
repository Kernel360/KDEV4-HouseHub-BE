package com.househub.backend.domain.inquiry.dto;

import java.util.List;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInquiryReqDto {

	@NotBlank
	private String templateToken;

	@NotBlank
	private String name;

	@Email
	private String email;

	@NotBlank
	private String phone;

	@NotEmpty
	private List<AnswerDto> answers;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class AnswerDto {
		private Long questionId;
		private Object answerText; // String, List<String> 모두 가능
	}

	public CreateCustomerReqDto toCustomerReqDto() {
		return CreateCustomerReqDto.builder()
			.name(this.name)
			.email(this.email)
			.contact(this.phone)
			.build();
	}

}

