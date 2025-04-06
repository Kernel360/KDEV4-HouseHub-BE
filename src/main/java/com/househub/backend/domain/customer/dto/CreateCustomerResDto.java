package com.househub.backend.domain.customer.dto;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.customer.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerResDto{
    private Long id;
    private String name;
    private Integer ageGroup;
    private String contact;
    private String email;
    private String memo;
    private Gender gender;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Customer toEntity() {
        return Customer.builder()
                .name(this.name)
                .ageGroup(this.ageGroup)
                .contact(this.contact)
                .email(this.email)
                .memo(this.memo)
                .gender(this.gender)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .build();
    }

}
