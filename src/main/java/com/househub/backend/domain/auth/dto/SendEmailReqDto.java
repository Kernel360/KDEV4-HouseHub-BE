package com.househub.backend.domain.auth.dto;

import com.househub.backend.domain.auth.enums.TokenType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailReqDto {
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "잘못된 이메일 형식입니다.")
	private String email;

	@NotNull(message = "요청 타입을 입력해주세요.")
	private TokenType type;
}
