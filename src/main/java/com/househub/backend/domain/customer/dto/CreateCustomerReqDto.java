package com.househub.backend.domain.customer.dto;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerReqDto {

    private String name;

    private Integer ageGroup;

    @NotBlank(message = "연락처는 필수입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String contact;

    private String email;

    private String memo;

    private Gender gender;

    public Customer toEntity(Agent agent) {
        return Customer.builder()
                .name(this.name)
                .ageGroup(this.ageGroup != null ? this.ageGroup : null)
                .contact(this.contact)
                .email(this.email)
                .memo(this.memo)
                .gender(this.gender != null ? this.gender : null)
                .agent(agent)
                .build();
    }
}