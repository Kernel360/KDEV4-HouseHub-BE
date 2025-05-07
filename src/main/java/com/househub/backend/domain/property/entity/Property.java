package com.househub.backend.domain.property.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.tag.entity.Tag;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.enums.PropertyDirection;
import com.househub.backend.domain.property.enums.PropertyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "properties")
@SQLRestriction("deleted_at IS NULL") // 조회 시 자동으로 deletedAt == null 조건
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 매물 고유 식별자 (PK)

    @OneToMany(mappedBy = "property")
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
    private String jibunAddress; // 지번 주소

    private Double area; // 면적 (평수)
    private Integer floor; // 층수
    private Integer allFloors; // 총 층수
    private PropertyDirection direction; // 방향 (남, 북, 동, 서, 남동, 남서, 북동, 북서)
    private Integer bathroomCnt; // 화장실 개수
    private Integer roomCnt; // 방 개수

    @Column(nullable = false)
    private Boolean active; // 매물이 계약 가능한지 여부 default : true (계약이 없는 경우 true)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 등록일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    private LocalDateTime deletedAt; // 삭제일시 (소프트 삭제)

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude; // 위도

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude; // 경도

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PropertyTagMap> propertyTagMaps = new ArrayList<>();

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

    public void enable() {
        this.active = true;
    }

    public void disable() {
        this.active = false;
    }

    public void addTags(List<Tag> tags) {
        tags.forEach(t -> this.propertyTagMaps.add(
                PropertyTagMap.builder()
                        .tag(t)
                        .property(this)
                        .build()
        ));
    }

    public void update(UpdatePropertyReqDto updateDto, Customer customer) {
        if (updateDto.getCustomerId() != null) this.customer = customer;
        if (updateDto.getPropertyType() != null) this.propertyType = updateDto.getPropertyType();
        if (updateDto.getMemo() != null) this.memo = updateDto.getMemo();

        if (updateDto.getArea() != null) this.area = updateDto.getArea();
        if (updateDto.getFloor() != null) this.floor = updateDto.getFloor();
        if (updateDto.getAllFloors() != null) this.allFloors = updateDto.getAllFloors();
        if (updateDto.getDirection() != null) this.direction = updateDto.getDirection();
        if (updateDto.getBathroomCnt() != null) this.bathroomCnt = updateDto.getBathroomCnt();
        if (updateDto.getRoomCnt() != null) this.roomCnt = updateDto.getRoomCnt();
        if (updateDto.getActive() != null) this.active = updateDto.getActive();

        if (updateDto.getRoadAddress() != null) this.roadAddress = updateDto.getRoadAddress();
        if (updateDto.getJibunAddress() != null) {
            this.jibunAddress = updateDto.getJibunAddress();
            parseJibunAddress(updateDto.getJibunAddress());
        }
        if (updateDto.getDetailAddress() != null) this.detailAddress = updateDto.getDetailAddress();
    }

    // 삭제 메서드
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        // 매물 삭제 시, 해당 계약도 모두 소프트 딜리트 해야함
        contracts.forEach(Contract::softDelete);
    }
}
