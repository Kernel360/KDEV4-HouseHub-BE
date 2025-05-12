package com.househub.backend.domain.notification.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.domain.notification.dto.EmailReqDto;
import com.househub.backend.domain.notification.entity.Notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Async
	public void sendVerificationCode(String email, String verificationCode) {
		log.info("이메일 인증 코드 전송 시작 - email={}", email);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(fromEmail);
		message.setSubject("HouseHub 인증 코드 안내");

		String emailBody = "안녕하세요!\n\n" + "HouseHub에 가입해 주셔서 감사합니다! \n\n"
			+ "서비스를 이용하시려면 아래의 인증 코드를 입력해 주세요:\n\n" + " **인증 코드:** " + verificationCode
			+ "\n\n" + "이 인증 코드는 서비스 가입을 완료하는 데 필요하며, 한 번만 사용 가능합니다.\n"
			+ "감사합니다!\n\n" + "HouseHub 팀 드림";

		message.setText(emailBody);

		try {
			mailSender.send(message);
			log.info("이메일 인증 코드 전송 완료 - email={}", email);
		} catch (MailException e) {
			log.info("이메일 인증 코드 전송 실패 - email={}, message={}", email, e.getMessage());
			throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);

		}
	}

	@Async
	public CompletableFuture<Void> send(Notification notification) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(notification.getReceiver().getEmail()); // 실제 수신자 이메일 주소
		message.setSubject("새로운 알림");
		message.setText(notification.getContent());

		try {
			mailSender.send(message);
			return CompletableFuture.completedFuture(null); // 정상 종료
		} catch (MailException e) {
			// 예외 로깅
			log.error("메일 전송 실패: {}", e.getMessage());
			// 예외를 CompletableFuture로 반환
			return CompletableFuture.failedFuture(new BusinessException(ErrorCode.EMAIL_SEND_FAILED));
		}
	}

	@Async
	public CompletableFuture<Void> sendEmail(EmailReqDto request) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

			helper.setFrom(fromEmail);
			helper.setTo(request.getTo());
			helper.setSubject(request.getSubject());
			helper.setText(request.getBody(), true); // true -> HTML

			mailSender.send(message);
			log.info("이메일 전송 완료 - to={}", request.getTo());
			return CompletableFuture.completedFuture(null);
		} catch (MessagingException | MailException e) {
			log.error("이메일 전송 실패 - to={}, error={}", request.getTo(), e.getMessage());
			return CompletableFuture.failedFuture(new BusinessException(ErrorCode.EMAIL_SEND_FAILED));
		}
	}
}
