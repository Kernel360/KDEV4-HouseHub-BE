package com.househub.backend.domain.customer.dto;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.customer.entity.Customer;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerReqDto{

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 50, message= "이름은 2자 이상 50자 이하 여야 합니다.")
    private String name;

    @NotNull(message = "연령대는 필수입니다.")
    @Min(value = 0, message = "연령대는 0 이상이어야 합니다.")
    @Max(value = 100, message = "연령대는 100 이하여야 합니다.")
    private Integer ageGroup;

    @NotBlank(message = "연락처는 필수입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String contact;


    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String memo;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    public Customer toEntity() {
        return Customer.builder()
                .name(this.name)
                .ageGroup(this.ageGroup)
                .contact(this.contact)
                .email(this.email)
                .memo(this.memo)
                .gender(this.gender)
                .build();
    }
}