package com.househub.backend.domain.crawlingProperty.dto;

import java.util.List;

import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrawlingPropertyReqDto {

	private PropertyType propertyType;

	private TransactionType transactionType;

	private String province;

	private String city;

	private String dong;

	private String detailAddress;

	private Float minArea;

	private Float maxArea;

	private Float minSalePrice;

	private Float maxSalePrice;

	private Float minDeposit;

	private Float maxDeposit;

	private Float minMonthlyRent;

	private Float maxMonthlyRent;

	private List<Long> tagIds;

	@Override
	public String toString() {
		return "CrawlingPropertyReqDto{" +
			"propertyType=" + propertyType +
			", transactionType=" + transactionType +
			", province='" + province + '\'' +
			", city='" + city + '\'' +
			", dong='" + dong + '\'' +
			", detailAddress='" + detailAddress + '\'' +
			", minArea=" + minArea +
			", maxArea=" + maxArea +
			", minSalePrice=" + minSalePrice +
			", maxSalePrice=" + maxSalePrice +
			", minDeposit=" + minDeposit +
			", maxDeposit=" + maxDeposit +
			", minMonthlyRent=" + minMonthlyRent +
			", maxMonthlyRent=" + maxMonthlyRent +
			", tagIds=" + tagIds +
			'}';
	}
}
