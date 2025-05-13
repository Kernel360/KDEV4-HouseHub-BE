package com.househub.backend.domain.consultation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.customer.entity.Customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ConsultationReqDto {

	@NotNull(message = "공인중개사 id를 입력하세요.")
	private Long agentId;

	private Long customerId;

	@Valid
	@JsonProperty("newCustomer")
	private NewCustomerDto newCustomer;

	@NotNull(message = "상담 수단을 입력하세요.")
	private ConsultationType consultationType; // PHONE, VISIT

	private String content;

	private LocalDateTime consultationDate;

	@NotNull(message = "상담 상태를 입력하세요.")
	private ConsultationStatus status; // RESERVED, COMPLETED, CANCELED

	private List<Long> selectedPropertyIds;

	public Consultation toEntity(Agent agent, Customer customer) {
		return Consultation.builder()
			.agent(agent)
			.customer(customer)
			.consultationType(this.getConsultationType())
			.content(this.getContent())
			.consultationDate(this.getConsultationDate())
			.status(this.getStatus())
			.build()
			;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	public static class NewCustomerDto {
		private String name;

		@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
		private String contact;

		private String email;

		public Customer toEntity(Agent agent) {
			return Customer.builder()
				.agent(agent)
				.name(this.getName())
				.contact(this.getContact())
				.email(this.getEmail())
				.build();
		}
	}
}






