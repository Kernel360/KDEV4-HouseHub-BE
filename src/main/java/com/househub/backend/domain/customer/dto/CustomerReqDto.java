package com.househub.backend.domain.customer.dto;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.validation.ValidBirthDate;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.enums.CustomerStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReqDto {

    private String name;

	@ValidBirthDate
	private LocalDate birthDate;

	@NotBlank(message = "연락처는 필수입니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
	private String contact;

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	private String memo;

	private Gender gender;

	public Customer toEntity(Agent agent) {
		return Customer.builder()
			.name(this.name)
			.birthDate(this.birthDate != null ? this.birthDate : null)
			.contact(this.contact)
			.email(this.email)
			.memo(this.memo)
			.gender(this.gender != null ? this.gender : null)
			.agent(agent)
			.status(CustomerStatus.POTENTIAL)
			.build();
	}
}