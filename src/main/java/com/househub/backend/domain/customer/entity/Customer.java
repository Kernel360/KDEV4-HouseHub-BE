package com.househub.backend.domain.customer.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer ageGroup;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

	public void update(CreateCustomerReqDto reqDto) {
		Optional.ofNullable(reqDto.getName()).ifPresent(name -> this.name = name);
		Optional.ofNullable(reqDto.getEmail()).ifPresent(email -> this.email = email);
		Optional.ofNullable(reqDto.getAgeGroup()).ifPresent(ageGroup -> this.ageGroup = ageGroup);
		Optional.ofNullable(reqDto.getMemo()).ifPresent(memo -> this.memo = memo);
		Optional.ofNullable(reqDto.getContact()).ifPresent(contact -> this.contact = contact);
		Optional.ofNullable(reqDto.getGender()).ifPresent(gender -> this.gender = gender);
	}

    public CreateCustomerResDto toDto() {
        return CreateCustomerResDto.builder()
                .name(this.getName())
                .ageGroup(this.getAgeGroup())
                .contact(this.getContact())
                .email(this.getEmail())
                .memo(this.getMemo())
                .gender(this.getGender())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .deletedAt(this.getDeletedAt())
                .build();
    }
}
