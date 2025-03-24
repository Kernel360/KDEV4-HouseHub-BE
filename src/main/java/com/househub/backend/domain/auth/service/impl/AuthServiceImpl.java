package com.househub.backend.domain.auth.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.entity.Role;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.auth.dto.SignUpRequestDto;
import com.househub.backend.domain.auth.service.AuthService;
import com.househub.backend.domain.realEstate.repository.RealEstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AgentRepository agentRepository;
    private final RealEstateRepository realEstateRepository;


    /**
     * 부동산 공인중개사 회원가입
     *
     * @param request 회원가입 요청 정보
     */
    @Transactional
    @Override
    public void signup(SignUpRequestDto request) {
        // 중개사의 자격증 번호 이미 존재하는지 확인
        validateLicenseNumber(request.getAgent().getLicenseNumber());

        // 부동산 사업자 등록 번호 이미 존재하는지 확인
        // 존재하지 않으면 부동산 정보 새로 저장
        RealEstate realEstate = getOrCreateRealEstate(request.getRealEstate());

        // 증개사 정보 저장
        saveAgent(request.getAgent(), realEstate);
    }

    /**
     * 자격증 번호의 중복 여부를 검증합니다.
     *
     * @param licenseNumber 검증할 자격증 번호
     * @throws AlreadyExistsException 자격증 번호가 이미 존재하는 경우 발생
     */
    private void validateLicenseNumber(String licenseNumber) {
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
    private RealEstate getOrCreateRealEstate(SignUpRequestDto.RealEstateDto realEstateDto) {
        return realEstateRepository.findByBusinessRegistrationNumber(realEstateDto.getBusinessRegistrationNumber())
                .orElseGet(() -> realEstateRepository.save(toRealEstateEntity(realEstateDto)));
    }

    /**
     * 공인중개사 정보를 저장합니다.
     *
     * @param agentDto 공인중개사 정보 DTO
     * @param realEstate 부동산 엔티티
     */
    private void saveAgent(SignUpRequestDto.AgentDto agentDto, RealEstate realEstate) {
        agentRepository.save(toAgentEntity(agentDto, realEstate));
    }

    /**
     * RequestDto 의 부동산 정보를 RealEstate 엔티티로 변환합니다.
     *
     * @param realEstateDTO 부동산 정보 DTO
     * @return RealEstate 엔티티
     */
    private RealEstate toRealEstateEntity(SignUpRequestDto.RealEstateDto realEstateDTO) {
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
    private Agent toAgentEntity(SignUpRequestDto.AgentDto agentDTO, RealEstate realEstate) {
        return Agent.builder()
                .name(agentDTO.getName())
                .licenseNumber(agentDTO.getLicenseNumber())
                .email(agentDTO.getEmail())
                .password(agentDTO.getPassword())
                .contact(agentDTO.getContact())
                .realEstate(realEstate)
                .role(Role.AGENT)
                .status(AgentStatus.PENDING)
                .build();
    }
}
