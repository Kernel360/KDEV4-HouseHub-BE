package com.househub.backend.domain.auth.dto;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.RealEstate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpReqDto {

	@Valid
	private AgentDto agent; // 공인중개사 정보

	@Valid
	private RealEstateDto realEstate; // 부동산 정보 (선택값)

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class AgentDto {
		@NotBlank(message = "이름을 입력하세요.")
		private String name;

		@Pattern(regexp = "^(서울|부산|대구|인천|광주|대전|울산|세종|경기|강원|충북|충남|전북|전남|경북|경남|제주)-(\\d{4})-(\\d{5})$", message = "잘못된 자격증 번호 형식입니다. (예: 123-45-67890)")
		private String licenseNumber; // 선택값

		@NotBlank(message = "이메일을 입력해주세요.")
		@Email(message = "잘못된 이메일 형식입니다.")
		private String email;

		@NotBlank(message = "비밀번호를 입력해주세요.")
		@Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
		@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
		private String password;

		@NotBlank(message = "연락처를 입력해주세요.")
		@Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "잘못된 연락처 형식입니다. (예: 010-1234-5678)")
		private String contact;

		private boolean isEmailVerified; // 이메일 인증 여부, 필수, 반드시 true 여야 회원가입 가능.

		public Agent toAgentEntity(RealEstate realEstate) {
			return Agent.builder()
				.name(name)
				.licenseNumber(licenseNumber)
				.email(email)
				.password(password)
				.contact(contact)
				.realEstate(realEstate)
				.build();
		}

		@Override
		public String toString() {
			return "AgentDTO{" +
				"name='" + name + '\'' +
				", licenseNumber='" + licenseNumber + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", contact='" + contact + '\'' +
				'}';
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class RealEstateDto {
		private String name; // 선택값

		@Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "잘못된 사업자 등록번호 형식입니다. (예: 123-45-67890)")
		private String businessRegistrationNumber; // 선택값

		private String address; // 선택값

		private String roadAddress; // 선택값

		@Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}", message = "잘못된 연락처 형식입니다. (예: 02-1234-5678)")
		private String contact;

		@Override
		public String toString() {
			return "RealEstateDTO{" +
				"name='" + name + '\'' +
				", businessRegistrationNumber='" + businessRegistrationNumber + '\'' +
				", address='" + address + '\'' +
				", roadAddress='" + roadAddress + '\'' +
				", contact='" + contact + '\'' +
				'}';
		}

		public RealEstate toRealEstateEntity() {
			return RealEstate.builder()
				.name(name)
				.businessRegistrationNumber(businessRegistrationNumber)
				.address(address)
				.roadAddress(roadAddress)
				.contact(contact)
				.build();
		}
	}

	@Override
	public String toString() {
		return "SignUpRequest{" +
			"agent=" + agent +
			", realEstate=" + realEstate +
			'}';
	}
}
