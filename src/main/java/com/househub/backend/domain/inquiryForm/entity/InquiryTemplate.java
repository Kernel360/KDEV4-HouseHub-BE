package com.househub.backend.domain.inquiryForm.entity;

import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inquiry_templates")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "inquiryTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    private boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public static InquiryTemplate fromDto(CreateInquiryTemplateReqDto reqDto) {
        return InquiryTemplate.builder()
                .name(reqDto.getName())
                .description(reqDto.getDescription())
                .isActive(reqDto.isActive())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
