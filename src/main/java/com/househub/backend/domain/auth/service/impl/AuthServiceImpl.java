package com.househub.backend.domain.auth.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.common.util.SessionManager;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.enums.Role;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.dto.SignUpReqDto;
import com.househub.backend.domain.auth.exception.EmailVerifiedException;
import com.househub.backend.domain.auth.exception.InvalidPasswordException;
import com.househub.backend.domain.auth.service.AuthCode;
import com.househub.backend.domain.auth.service.AuthService;
import com.househub.backend.domain.realEstate.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AgentRepository agentRepository;
	private final RealEstateRepository realEstateRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final SessionManager sessionManager;
	private final AuthCode authCode;

	/**
	 * 부동산 공인중개사 회원가입
	 *
	 * @param request 회원가입 요청 정보
	 * @throws EmailVerifiedException 이메일 인증이 완료되지 않은 경우 발생
	 */
	@Transactional
	@Override
	public void signup(SignUpReqDto request) {
		SignUpReqDto.AgentDto agentDto = request.getAgent();
		Optional<SignUpReqDto.RealEstateDto> realEstateDtoOptional = Optional.ofNullable(request.getRealEstate());

		// 이메일 인증 여부에 따른 예외 처리
		validateEmailVerification(agentDto);

		// 이메일 중복 여부 검증
		checkEmailDuplication(agentDto.getEmail());

		// 자격증번호 값이 들어오면, 중개사의 자격증 번호 이미 존재하는지 확인
		validateLicenseNumber(agentDto.getLicenseNumber());

		// 부동산 정보가 입력받은 게 없으면 기본값(null) 설정
		// 부동산 사업자 등록 번호 이미 존재하는지 확인
		// 존재하지 않으면 부동산 정보 새로 저장
		RealEstate realEstate = realEstateDtoOptional.map(this::getOrCreateRealEstate).orElse(null);

		// 증개사 정보 저장
		saveAgent(request.getAgent(), realEstate);
	}

	/**
	 * 에이전트 로그인 처리.
	 *
	 * @param request 로그인 요청 정보 (이메일, 비밀번호)
	 * @return 로그인 성공 시 에이전트 정보 (ID, 이메일)
	 * @throws ResourceNotFoundException     해당 이메일의 사용자를 찾을 수 없는 경우
	 * @throws InvalidPasswordException 비밀번호가 일치하지 않는 경우
	 */
	@Transactional
	@Override
	public SignInResDto signin(SignInReqDto request) {
		try {
			log.info("사용자 인증 시도");
			// 사용자 인증
			Authentication authentication = authenticateUser(request);

			// 사용자 정보 조회
			Agent existingAgent = findAgentByEmail(request.getEmail());

			// 세션 관리
			sessionManager.manageAgentSession(authentication);

			return SignInResDto.builder()
				.id(existingAgent.getId())
				.email(existingAgent.getEmail())
				.build();
		} catch (InternalAuthenticationServiceException ex) {
			log.warn("로그인 실패 - 사용자를 찾을 수 없음: {}", request.getEmail());
			throw new BusinessException(ErrorCode.EMAIL_MISMATCH);
		} catch (BadCredentialsException ex) {
			log.warn("로그인 실패 - 잘못된 자격 증명: {}", request.getEmail());
			throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
		} catch (Exception ex) {
			log.error("로그인 중 예외 발생: {}", ex.getMessage(), ex);
			throw ex;
		}
	}

	private Authentication authenticateUser(SignInReqDto request) {
		// 이메일 대소문자 구분 없이 비교하기 위해 소문자로 변환
		String normalizedEmail = request.getEmail().toLowerCase();
		UsernamePasswordAuthenticationToken token =
			new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword());
		return authenticationManager.authenticate(token);
	}

	private Agent findAgentByEmail(String email) {
		return agentRepository.findByEmail(email)
			.orElseThrow(() -> new ResourceNotFoundException(
				"해당 이메일의 사용자를 찾을 수 없습니다.",
				"AGENT_NOT_FOUND"
			));
	}

	/**
	 * 이메일 인증 여부를 확인하고, 인증되지 않은 경우 예외를 발생시킵니다.
	 *
	 * @param agentDto 회원가입 요청에 포함된 중개사 정보
	 * @throws EmailVerifiedException 이메일 인증이 완료되지 않은 경우 발생
	 */
	private void validateEmailVerification(SignUpReqDto.AgentDto agentDto) {
		log.info("{}", agentDto.toString());
		if (!agentDto.isEmailVerified()) {
			throw new EmailVerifiedException("이메일 인증이 완료되지 않았습니다.", "EMAIL_NOT_VERIFIED");
		}
	}

	/**
	 * 이메일 중복 여부를 확인하고, 이미 등록된 이메일이 있는 경우 예외를 발생시킵니다.
	 *
	 * @param email 중복을 확인할 이메일
	 * @throws AlreadyExistsException 이메일이 이미 등록되어 있을 경우 발생
	 */
	private void checkEmailDuplication(String email) {
		Optional<Agent> existingAgent = agentRepository.findByEmail(email);
		if (existingAgent.isPresent()) {
			throw new AlreadyExistsException("이미 등록된 이메일입니다.", "EMAIL_ALREADY_EXISTS");
		}
	}

	/**
	 * 자격증 번호의 중복 여부를 검증합니다.
	 *
	 * @param licenseNumber 검증할 자격증 번호
	 * @throws AlreadyExistsException 자격증 번호가 이미 존재하는 경우 발생
	 */
	private void validateLicenseNumber(String licenseNumber) {
		if (!StringUtils.hasText(licenseNumber)) {
			return; // 자격증 번호가 없으면 검사할 필요 없음.
		}
		agentRepository.findByLicenseNumber(licenseNumber)
			.ifPresent(agent -> {
				log.error("이미 존재하는 자격증 번호: {}", agent.getLicenseNumber());
				throw new AlreadyExistsException("이미 존재하는 자격증 번호입니다.", "LICENSE_NUMBER_ALREADY_EXISTS");
			});
	}

	/**
	 * 부동산 사업자 등록 번호의 존재 여부를 확인하고, 존재하지 않으면 새로운 부동산 정보를 저장합니다.
	 *
	 * @param realEstateDto 부동산 정보 DTO
	 * @return 저장된 또는 존재하는 부동산 엔티티
	 */
	private RealEstate getOrCreateRealEstate(SignUpReqDto.RealEstateDto realEstateDto) {
		return realEstateRepository.findByBusinessRegistrationNumber(realEstateDto.getBusinessRegistrationNumber())
			.orElseGet(() -> realEstateRepository.save(toRealEstateEntity(realEstateDto)));
	}

	/**
	 * 공인중개사 정보를 저장합니다.
	 *
	 * @param agentDto 공인중개사 정보 DTO
	 * @param realEstate 부동산 엔티티
	 */
	private void saveAgent(SignUpReqDto.AgentDto agentDto, RealEstate realEstate) {
		agentRepository.save(toAgentEntity(agentDto, realEstate));
	}

	/**
	 * RequestDto 의 부동산 정보를 RealEstate 엔티티로 변환합니다.
	 *
	 * @param realEstateDTO 부동산 정보 DTO
	 * @return RealEstate 엔티티
	 */
	private RealEstate toRealEstateEntity(SignUpReqDto.RealEstateDto realEstateDTO) {
		return RealEstate.builder()
			.name(realEstateDTO.getName())
			.businessRegistrationNumber(realEstateDTO.getBusinessRegistrationNumber())
			.address(realEstateDTO.getAddress())
			.roadAddress(realEstateDTO.getRoadAddress())
			.contact(realEstateDTO.getContact())
			.build();
	}

	/**
	 * RequestDto 의 공인중개사 정보를 Agent 엔티티로 변환합니다.
	 *
	 * @param agentDTO 공인중개사 정보 DTO
	 * @param realEstate 부동산 엔티티
	 * @return Agent 엔티티
	 */
	private Agent toAgentEntity(SignUpReqDto.AgentDto agentDTO, RealEstate realEstate) {
		return Agent.builder()
			.name(agentDTO.getName())
			.licenseNumber(agentDTO.getLicenseNumber())
			.email(agentDTO.getEmail())
			.password(passwordEncoder.encode(agentDTO.getPassword()))
			.contact(agentDTO.getContact())
			.realEstate(realEstate)
			.role(Role.AGENT)
			.build();
	}

	/**
	 * 인증 코드를 생성하고 Redis에 저장합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 생성된 인증 코드
	 */
	@Override
	public String generateAndSaveAuthCode(String email) {
		String code = authCode.generateAuthCode();
		authCode.saveAuthCode(email, code);
		return code;
	}

	/**
	 * Redis에서 인증 코드를 조회합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 조회된 인증 코드 (null이면 인증 코드가 존재하지 않음)
	 */
	@Override
	public String getAuthCode(String email) {
		return authCode.getAuthCode(email);
	}

	/**
	 * Redis에서 인증 코드를 삭제합니다.
	 *
	 * @param email 사용자 이메일
	 */
	@Override
	public void deleteAuthCode(String email) {
		authCode.deleteAuthCode(email);
	}

	/**
	 * 인증 코드를 검증합니다.
	 *
	 * @param email 사용자 이메일
	 * @param code  인증 코드
	 */
	@Transactional
	@Override
	public void verifyCode(String email, String code) {
		// Redis 에서 인증번호 조회
		String storedAuthCode = getAuthCode(email);

		// 인증번호 만료
		if (storedAuthCode == null) {
			throw new BusinessException(ErrorCode.AUTH_CODE_EXPIRED);
		}

		// 인증번호 비교
		if (!storedAuthCode.equals(code)) {
			throw new BusinessException(ErrorCode.AUTH_CODE_MISMATCH);
		}

		// 인증번호 삭제
		deleteAuthCode(email);
	}

	/**
	 * 이메일 중복 여부를 확인합니다.
	 * @param email 중복을 확인할 이메일
	 */
	@Override
	public void checkEmailAlreadyExists(String email) {
		if (agentRepository.existsByEmail(email)) {
			throw new AlreadyExistsException("이미 가입된 이메일입니다.", "EMAIL_ALREADY_EXISTS");
		}
	}
}
