package com.househub.backend.domain.auth.service;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionBasedUserDetailsService implements UserDetailsService {
    private final AgentRepository agentRepository;

    /**
     * 주어진 이메일을 사용하여 사용자 정보를 로드합니다.
     *
     * @param email 사용자 이메일
     * @return UserDetails 객체 (인증에 필요한 사용자 정보)
     * @throws ResourceNotFoundException 해당 이메일의 사용자를 찾을 수 없는 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {
        Optional<Agent> existingAgent = agentRepository.findByEmail(email);

        if (existingAgent.isEmpty()) {
            throw new ResourceNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다.", "AGENT_NOT_FOUND");
        }

        Agent signInAgent = existingAgent.get();
        return org.springframework.security.core.userdetails.User
                .withUsername(signInAgent.getEmail())
                .password(signInAgent.getPassword())
                .roles(String.valueOf(signInAgent.getRole()))
                .build();
    }
}
