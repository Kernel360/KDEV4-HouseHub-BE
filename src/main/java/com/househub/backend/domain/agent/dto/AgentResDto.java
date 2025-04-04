package com.househub.backend.domain.agent.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AgentResDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private Role role;
    private AgentStatus status;

    public static AgentResDto fromEntity(Agent agent) {
        return AgentResDto.builder()
                .id(agent.getId())
                .email(agent.getEmail())
                .password(agent.getPassword())
                .name(agent.getName())
                .role(agent.getRole())
                .status(agent.getStatus())
                .build();
    }
}
