package com.househub.backend.domain.property.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionUpdateReqDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "property_conditions")
public class PropertyCondition {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 매물 조건 고유 식별자 (PK)

	@ManyToOne
	@JoinColumn(name = "propertyId", nullable = false)
	private Property property; // 매물

	@OneToMany(mappedBy = "propertyCondition", cascade = CascadeType.ALL)
	private List<Contract> contracts;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ContractType contractType; // 거래 유형 (매매, 전세, 월세)

	private Long salePrice; // 매매가
	private Long jeonsePrice; // 전세가
	private Integer monthlyRentFee; // 월세 금액
	private Integer monthlyRentDeposit; // 월세 보증금

	private String memo;

	@Column(nullable = false)
	private Boolean active; // 매물 조건 활성화 여부

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt; // 등록일시

	@Column(nullable = false)
	private LocalDateTime updatedAt; // 수정일시

	private LocalDateTime deletedAt; // 삭제일시 (소프트 삭제)

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	// 삭제 메서드
	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		// 해당 계약 리스트 소프트 딜리트
		for(Contract contract : contracts) {
			contract.softDelete();
		}
		// 해당 매물에 대해 모든 매물 조건이 삭제되면 매물 비활성화
		if (property.getConditions().stream().allMatch(c -> c.getDeletedAt() != null)) {
			property.updateActiveStatus(false);
		}
	}

	// 수정 메서드
	public void update(PropertyConditionUpdateReqDto updateReqDto) {
		if(updateReqDto.getContractType() != null) {
			this.contractType = updateReqDto.getContractType();
		}
		if(updateReqDto.getSalePrice() != null) {
			this.salePrice = updateReqDto.getSalePrice();
		}
		if(updateReqDto.getJeonsePrice() != null) {
			this.jeonsePrice = updateReqDto.getJeonsePrice();
		}
		if(updateReqDto.getMonthlyRentFee() != null) {
			this.monthlyRentFee = updateReqDto.getMonthlyRentFee();
		}
		if(updateReqDto.getMonthlyRentDeposit() != null) {
			this.monthlyRentDeposit = updateReqDto.getMonthlyRentDeposit();
		}
		if(updateReqDto.getMemo() != null) {
			this.memo = updateReqDto.getMemo();
		}
		if(updateReqDto.getActive() != null) {
			this.active = updateReqDto.getActive();
			if(!updateReqDto.getActive()) { // 비활성화로 바꿀 경우
				// 모든 매물 조건이 비활성화이면 매물도 비활성화
				if(property.getConditions().stream().allMatch(c -> c.getActive() == false)) {
					property.updateActiveStatus(false);
				}
			} else { // 활성화로 바꿀 경우
				property.updateActiveStatus(true);
			}
		}
	}

	public void updateActiveStatus(Boolean active) {
		this.active = active;
	}
}
