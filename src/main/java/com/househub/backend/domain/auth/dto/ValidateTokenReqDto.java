package com.househub.backend.domain.auth.dto;

import com.househub.backend.domain.auth.enums.TokenType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ValidateTokenReqDto {

	@NotBlank(message = "토큰은 필수입니다.")
	private String token;

	@NotBlank(message = "토큰 유형은 필수입니다.")
	private TokenType type; // PASSWORD_RESET, EMAIL_VERIFICATION 등
}

