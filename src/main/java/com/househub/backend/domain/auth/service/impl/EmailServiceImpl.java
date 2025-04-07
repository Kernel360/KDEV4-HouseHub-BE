package com.househub.backend.domain.auth.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.househub.backend.domain.auth.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Override
	public void sendVerificationCode(String email, String verificationCode) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(fromEmail);
		message.setSubject("HouseHub 인증 코드 안내");

		String emailBody = "안녕하세요!\n\n" + "HouseHub에 가입해 주셔서 감사합니다! \n\n"
			+ "서비스를 이용하시려면 아래의 인증 코드를 입력해 주세요:\n\n" + " **인증 코드:** " + verificationCode
			+ "\n\n" + "이 인증 코드는 서비스 가입을 완료하는 데 필요하며, 한 번만 사용 가능합니다.\n"
			+ "감사합니다!\n\n" + "HouseHub 팀 드림";

		message.setText(emailBody);
		mailSender.send(message);
	}
}
