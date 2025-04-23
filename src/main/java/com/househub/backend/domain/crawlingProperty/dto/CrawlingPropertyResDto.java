package com.househub.backend.domain.crawlingProperty.dto;

import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.enums.Direction;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CrawlingPropertyResDto {

    private String crawlingPropertiesId;
    private PropertyType propertyType;
    private TransactionType transactionType;

    private String province;
    private String city;
    private String dong;
    private String detailAddress;

    private Float area;
    private String floor;
    private String allFloors;
    private Float salePrice;
    private Float deposit;
    private Float monthlyRentFee;
    private Direction direction;
    private Integer bathRoomCnt;
    private Integer roomCnt;

    private String realEstateAgentId;
    private String realEstateAgentName;
    private String realEstateAgentContact;
    private String realEstateOfficeName;
    private String realEstateOfficeAddress;

    public static CrawlingPropertyResDto fromEntity(CrawlingProperty crawlingProperty) {
        return CrawlingPropertyResDto.builder()
                .crawlingPropertiesId(crawlingProperty.getCrawlingPropertiesId())
                .propertyType(crawlingProperty.getPropertyType())
                .transactionType(crawlingProperty.getTransactionType())
                .province(crawlingProperty.getProvince())
                .city(crawlingProperty.getCity())
                .dong(crawlingProperty.getDong())
                .detailAddress(crawlingProperty.getDetailAddress())
                .area(crawlingProperty.getArea())
                .floor(crawlingProperty.getFloor())
                .allFloors(crawlingProperty.getAllFloors())
                .salePrice(crawlingProperty.getSalePrice())
                .deposit(crawlingProperty.getDeposit())
                .monthlyRentFee(crawlingProperty.getMonthlyRentFee())
                .direction(crawlingProperty.getDirection())
                .bathRoomCnt(crawlingProperty.getBathRoomCnt())
                .roomCnt(crawlingProperty.getRoomCnt())
                .realEstateAgentId(crawlingProperty.getRealEstateAgentId())
                .realEstateAgentName(crawlingProperty.getRealEstateAgentName())
                .realEstateAgentContact(crawlingProperty.getRealEstateAgentContact())
                .realEstateOfficeName(crawlingProperty.getRealEstateOfficeName())
                .realEstateOfficeAddress(crawlingProperty.getRealEstateOfficeAddress())
                .build();
    }
}
