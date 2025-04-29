package com.househub.backend.domain.inquiry.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.inquiry.entity.Inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDetailResDto {

	private Long inquiryId;
	private String customerName;
	private String customerEmail;
	private String customerContact;
	private String createdAt;
	private List<AnswerDto> answers;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AnswerDto {
		private Long questionId;
		private String questionContent;
		private String answer;
	}

	public static InquiryDetailResDto fromEntity(Inquiry inquiry) {
		Customer customer = inquiry.getCustomer();
		String name = customer.getName() != null ? customer.getName() : "미입력";
		String email = customer.getEmail() != null ? customer.getEmail() : "미입력";
		String contact = customer.getContact() != null ? customer.getContact() : "미입력";

		List<AnswerDto> answers = inquiry.getAnswers().stream()
			.map(a -> AnswerDto.builder()
				.questionId(a.getQuestion().getId())
				.questionContent(a.getQuestion().getLabel())
				.answer(a.getAnswer())
				.build())
			.collect(Collectors.toList());

		return InquiryDetailResDto.builder()
			.inquiryId(inquiry.getId())
			.customerName(name)
			.customerEmail(email)
			.customerContact(contact)
			.createdAt(inquiry.getCreatedAt().toString())
			.answers(answers)
			.build();
	}
}

