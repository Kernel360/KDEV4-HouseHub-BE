package com.househub.backend.domain.crawlingProperty.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCrawlingProperty is a Querydsl query type for CrawlingProperty
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCrawlingProperty extends EntityPathBase<CrawlingProperty> {

    private static final long serialVersionUID = -1852799551L;

    public static final QCrawlingProperty crawlingProperty = new QCrawlingProperty("crawlingProperty");

    public final StringPath allFloors = createString("allFloors");

    public final NumberPath<Float> area = createNumber("area", Float.class);

    public final NumberPath<Integer> bathRoomCnt = createNumber("bathRoomCnt", Integer.class);

    public final StringPath city = createString("city");

    public final StringPath crawlingPropertiesId = createString("crawlingPropertiesId");

    public final NumberPath<Float> deposit = createNumber("deposit", Float.class);

    public final StringPath detailAddress = createString("detailAddress");

    public final EnumPath<com.househub.backend.domain.crawlingProperty.enums.Direction> direction = createEnum("direction", com.househub.backend.domain.crawlingProperty.enums.Direction.class);

    public final StringPath dong = createString("dong");

    public final StringPath floor = createString("floor");

    public final NumberPath<Float> monthlyRentFee = createNumber("monthlyRentFee", Float.class);

    public final EnumPath<com.househub.backend.domain.crawlingProperty.enums.PropertyType> propertyType = createEnum("propertyType", com.househub.backend.domain.crawlingProperty.enums.PropertyType.class);

    public final StringPath province = createString("province");

    public final StringPath realEstateAgentContact = createString("realEstateAgentContact");

    public final StringPath realEstateAgentId = createString("realEstateAgentId");

    public final StringPath realEstateAgentName = createString("realEstateAgentName");

    public final StringPath realEstateOfficeAddress = createString("realEstateOfficeAddress");

    public final StringPath realEstateOfficeName = createString("realEstateOfficeName");

    public final NumberPath<Integer> roomCnt = createNumber("roomCnt", Integer.class);

    public final NumberPath<Float> salePrice = createNumber("salePrice", Float.class);

    public final EnumPath<com.househub.backend.domain.crawlingProperty.enums.TransactionType> transactionType = createEnum("transactionType", com.househub.backend.domain.crawlingProperty.enums.TransactionType.class);

    public QCrawlingProperty(String variable) {
        super(CrawlingProperty.class, forVariable(variable));
    }

    public QCrawlingProperty(Path<? extends CrawlingProperty> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCrawlingProperty(PathMetadata metadata) {
        super(CrawlingProperty.class, metadata);
    }

}

