package com.househub.backend.domain.auth.dto;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.enums.AgentStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
public class CustomAgentDetails implements UserDetails, Serializable {

    private final AgentResDto agentDetailsDto;

    public CustomAgentDetails(Agent agent) {
        this.agentDetailsDto = AgentResDto.builder()
                .id(agent.getId())
                .email(agent.getEmail())
                .password(agent.getPassword())
                .name(agent.getName())
                .role(agent.getRole())
                .status(agent.getStatus())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(agentDetailsDto.getRole().getValue()));
    }

    @Override
    public String getPassword() {
        return agentDetailsDto.getPassword();
    }

    @Override
    public String getUsername() {
        return agentDetailsDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !agentDetailsDto.getStatus().equals(AgentStatus.BLOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // authenticationManager.authenticate(token); 호출 후 검증 과정에서 동작
    @Override
    public boolean isEnabled() {
        return agentDetailsDto.getStatus().equals(AgentStatus.ACTIVE);
    }
}

