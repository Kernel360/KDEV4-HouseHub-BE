package com.househub.backend.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPasswordReqDto {
	@NotBlank(message = "토큰은 필수입니다.")
	private String token;

	@NotBlank(message = "새 비밀번호는 필수입니다.")
	private String newPassword;
}
