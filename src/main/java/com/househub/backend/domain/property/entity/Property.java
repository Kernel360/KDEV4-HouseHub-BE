package com.househub.backend.domain.property.entity;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.dto.PropertyReqDto;
import com.househub.backend.domain.property.enums.PropertyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "properties")
@Where(clause = "deleted_at IS NULL") // 조회 시 자동으로 deletedAt == null 조건
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId; // 매물 고유 식별자 (PK)

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Contract> contracts;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer; // 의뢰인 (임대인 또는 매도인)

    @ManyToOne
    @JoinColumn(name = "agentId", nullable = false)
    private Agent agent; // 매물을 등록한 공인중개사

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType; // 매물 유형 (아파트, 오피스텔, 상가, 원룸, 투룸, 빌라)

    private String memo;

    private String province; // 도, 특별시, 광역시
    private String city; // 시/군/구
    private String dong; // 읍/면/동
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 등록일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    private LocalDateTime deletedAt; // 삭제일시 (소프트 삭제)

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude; // 위도

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude; // 경도

    // 지번주소 -> 도, 시, 동 으로 파싱
    public void parseJibunAddress(String jibun) {
        String[] parts = jibun.split(" ");
        this.province = parts[0];
        this.city = parts[1];
        this.dong = parts[2];
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 수정 메서드 (setter 대신 사용)
    public void updateProperty(PropertyReqDto updateDto) {
        if (updateDto.getPropertyType() != null) this.propertyType = updateDto.getPropertyType();
        if (updateDto.getMemo() != null) this.memo = updateDto.getMemo();
        if (updateDto.getRoadAddress() != null) this.roadAddress = updateDto.getRoadAddress();
        if (updateDto.getDetailAddress() != null) this.detailAddress = updateDto.getDetailAddress();
        if (updateDto.getLatitude() != null) this.latitude = updateDto.getLatitude();
        if (updateDto.getLongitude() != null) this.longitude = updateDto.getLongitude();
        this.updatedAt = LocalDateTime.now();
        parseJibunAddress(updateDto.getJibunAddress());
    }

    // 삭제 메서드
    public void deleteProperty() {
        this.deletedAt = LocalDateTime.now();
    }
}
