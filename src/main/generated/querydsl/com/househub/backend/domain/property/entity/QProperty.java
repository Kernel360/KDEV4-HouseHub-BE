package com.househub.backend.domain.property.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProperty is a Querydsl query type for Property
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProperty extends EntityPathBase<Property> {

    private static final long serialVersionUID = 567202251L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProperty property = new QProperty("property");

    public final BooleanPath active = createBoolean("active");

    public final com.househub.backend.domain.agent.entity.QAgent agent;

    public final NumberPath<Integer> allFloors = createNumber("allFloors", Integer.class);

    public final NumberPath<Double> area = createNumber("area", Double.class);

    public final NumberPath<Integer> bathroomCnt = createNumber("bathroomCnt", Integer.class);

    public final StringPath city = createString("city");

    public final ListPath<com.househub.backend.domain.contract.entity.Contract, com.househub.backend.domain.contract.entity.QContract> contracts = this.<com.househub.backend.domain.contract.entity.Contract, com.househub.backend.domain.contract.entity.QContract>createList("contracts", com.househub.backend.domain.contract.entity.Contract.class, com.househub.backend.domain.contract.entity.QContract.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.househub.backend.domain.customer.entity.QCustomer customer;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath detailAddress = createString("detailAddress");

    public final EnumPath<com.househub.backend.domain.property.enums.PropertyDirection> direction = createEnum("direction", com.househub.backend.domain.property.enums.PropertyDirection.class);

    public final StringPath dong = createString("dong");

    public final NumberPath<Integer> floor = createNumber("floor", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath jibunAddress = createString("jibunAddress");

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    public final StringPath memo = createString("memo");

    public final EnumPath<com.househub.backend.domain.property.enums.PropertyType> propertyType = createEnum("propertyType", com.househub.backend.domain.property.enums.PropertyType.class);

    public final StringPath province = createString("province");

    public final StringPath roadAddress = createString("roadAddress");

    public final NumberPath<Integer> roomCnt = createNumber("roomCnt", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QProperty(String variable) {
        this(Property.class, forVariable(variable), INITS);
    }

    public QProperty(Path<? extends Property> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProperty(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProperty(PathMetadata metadata, PathInits inits) {
        this(Property.class, metadata, inits);
    }

    public QProperty(Class<? extends Property> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.househub.backend.domain.agent.entity.QAgent(forProperty("agent"), inits.get("agent")) : null;
        this.customer = inits.isInitialized("customer") ? new com.househub.backend.domain.customer.entity.QCustomer(forProperty("customer"), inits.get("customer")) : null;
    }

}

