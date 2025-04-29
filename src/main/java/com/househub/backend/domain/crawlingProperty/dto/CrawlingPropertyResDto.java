package com.househub.backend.domain.crawlingProperty.dto;

import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.entity.Tag;
import com.househub.backend.domain.crawlingProperty.enums.Direction;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

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
    private String salePrice;
    private String deposit;
    private String monthlyRentFee;
    private Direction direction;
    private Integer bathRoomCnt;
    private Integer roomCnt;

    private String realEstateAgentId;
    private String realEstateAgentName;
    private String realEstateAgentContact;
    private String realEstateOfficeName;
    private String realEstateOfficeAddress;


//    // 새로 추가한 부분
//    private List<TagDto> tags; // 태그 리스트
//
//    @Getter
//    @Builder
//    public static class TagDto {
//        private Long tagId;
//        private String type;
//        private String value;
//    }
//
//    public static CrawlingPropertyResDto fromEntity(CrawlingProperty crawlingProperty, Map<String, List<Tag>> propertyTagsMap) {
//        return CrawlingPropertyResDto.builder()
//                .crawlingPropertiesId(crawlingProperty.getCrawlingPropertiesId())
//                .propertyType(crawlingProperty.getPropertyType())
//                .transactionType(crawlingProperty.getTransactionType())
//                .province(crawlingProperty.getProvince())
//                .city(crawlingProperty.getCity())
//                .dong(crawlingProperty.getDong())
//                .detailAddress(crawlingProperty.getDetailAddress())
//                .area(crawlingProperty.getArea())
//                .floor(crawlingProperty.getFloor())
//                .allFloors(crawlingProperty.getAllFloors())
//                .salePrice(convertPriceFormat(crawlingProperty.getSalePrice())) // 가격 문자 형식으로 변환하여 반환
//                .deposit(convertPriceFormat(crawlingProperty.getDeposit())) // 가격 문자 형식으로 변환하여 반환
//                .monthlyRentFee(convertPriceFormat(crawlingProperty.getMonthlyRentFee())) // 가격 문자 형식으로 변환하여 반환
//                .direction(crawlingProperty.getDirection())
//                .bathRoomCnt(crawlingProperty.getBathRoomCnt())
//                .roomCnt(crawlingProperty.getRoomCnt())
//                .realEstateAgentId(crawlingProperty.getRealEstateAgentId())
//                .realEstateAgentName(crawlingProperty.getRealEstateAgentName())
//                .realEstateAgentContact(crawlingProperty.getRealEstateAgentContact())
//                .realEstateOfficeName(crawlingProperty.getRealEstateOfficeName())
//                .realEstateOfficeAddress(crawlingProperty.getRealEstateOfficeAddress())
//                .tags(
//                        propertyTagsMap.getOrDefault(crawlingProperty.getCrawlingPropertiesId(), List.of())
//                                .stream()
//                                .map(tagEntity -> TagDto.builder()
//                                        .tagId(tagEntity.getTagId())
//                                        .type(tagEntity.getType())
//                                        .value(tagEntity.getValue())
//                                        .build())
//                                .toList()
//                )
//                .build();
//    }

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
                .salePrice(convertPriceFormat(crawlingProperty.getSalePrice())) // 가격 문자 형식으로 변환하여 반환
                .deposit(convertPriceFormat(crawlingProperty.getDeposit())) // 가격 문자 형식으로 변환하여 반환
                .monthlyRentFee(convertPriceFormat(crawlingProperty.getMonthlyRentFee())) // 가격 문자 형식으로 변환하여 반환
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

    private static String convertPriceFormat(Float price) {

        if (price == null) return null;

        String convertedPrice, upperNumber, lowerNumber;

        long longPrice = price.longValue();
        String strPrice = String.valueOf(longPrice);

        int lenPrice = strPrice.length();
        if (lenPrice >= 5) { // 억단위 이상인 경우 처리
            upperNumber = strPrice.substring(0, lenPrice - 4);
            upperNumber = addComma(upperNumber) + "억 ";
            strPrice = strPrice.substring(lenPrice - 4);
        } else {
            upperNumber = "";
        }
        // 천만원 이하의 경우 처리
        if (strPrice.equals("0000")) {
            lowerNumber = "";
        } else {
            lowerNumber = addComma(String.valueOf(Integer.parseInt(strPrice)));
        }

        // 문자 합산
        convertedPrice = upperNumber + lowerNumber;

        return convertedPrice;
    }

    private static String addComma(String price) {
        String result;

        if (price.length() == 4) {
            result = price.substring(0, 1) + "," + price.substring(1);
        } else {
            result = price;
        }
        return result;
    }
}
