package com.househub.backend.domain.auth.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.common.exception.UnauthorizedException;
import com.househub.backend.common.util.SessionManager;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.entity.Role;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.dto.SignUpReqDto;
import com.househub.backend.domain.auth.exception.EmailVerifiedException;
import com.househub.backend.domain.auth.exception.InvalidPasswordException;
import com.househub.backend.domain.auth.service.AuthService;
import com.househub.backend.domain.realEstate.repository.RealEstateRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AgentRepository agentRepository;
    private final RealEstateRepository realEstateRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SessionManager sessionManager;


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

        // realEstateDto가 null인 경우 기본값 설정 또는 예외 처리
        SignUpReqDto.RealEstateDto realEstateDto = realEstateDtoOptional.orElse(null); // null 허용 또는 기본값 설정

        // 이메일 인증 여부에 따른 예외 처리
        validateEmailVerification(agentDto);

        // 이메일 중복 여부 검증
        checkEmailDuplication(agentDto.getEmail());

        // 자격증번호 값이 들어오면, 중개사의 자격증 번호 이미 존재하는지 확인
        validateLicenseNumber(agentDto.getLicenseNumber());

        // 부동산 사업자 등록 번호 이미 존재하는지 확인
        // 존재하지 않으면 부동산 정보 새로 저장
        RealEstate realEstate = getOrCreateRealEstate(realEstateDto);

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
            throw new UsernameNotFoundException("해당 이메일과 일치하는 중개사가 존재하지 않습니다.");
        } catch (BadCredentialsException ex) {
            log.warn("로그인 실패 - 잘못된 자격 증명: {}", request.getEmail());
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }  catch (Exception ex) {
            log.error("로그인 중 예외 발생: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private Authentication authenticateUser(SignInReqDto request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
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
}
