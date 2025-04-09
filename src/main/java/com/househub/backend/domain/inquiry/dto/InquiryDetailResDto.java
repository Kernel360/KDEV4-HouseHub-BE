package com.househub.backend.domain.inquiry.dto;

import java.util.List;
import java.util.stream.Collectors;

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
		String name =
			inquiry.getCustomer() != null ? inquiry.getCustomer().getName() : inquiry.getCandidate().getName();
		String email =
			inquiry.getCustomer() != null ? inquiry.getCustomer().getEmail() : inquiry.getCandidate().getEmail();
		String contact =
			inquiry.getCustomer() != null ? inquiry.getCustomer().getContact() : inquiry.getCandidate().getContact();

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

