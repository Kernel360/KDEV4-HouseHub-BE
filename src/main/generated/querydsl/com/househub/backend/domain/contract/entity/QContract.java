package com.househub.backend.domain.contract.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContract is a Querydsl query type for Contract
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContract extends EntityPathBase<Contract> {

    private static final long serialVersionUID = -1236197819L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContract contract = new QContract("contract");

    public final BooleanPath active = createBoolean("active");

    public final com.househub.backend.domain.agent.entity.QAgent agent;

    public final DatePath<java.time.LocalDate> completedAt = createDate("completedAt", java.time.LocalDate.class);

    public final EnumPath<com.househub.backend.domain.contract.enums.ContractType> contractType = createEnum("contractType", com.househub.backend.domain.contract.enums.ContractType.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.househub.backend.domain.customer.entity.QCustomer customer;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> expiredAt = createDate("expiredAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> jeonsePrice = createNumber("jeonsePrice", Long.class);

    public final StringPath memo = createString("memo");

    public final NumberPath<Integer> monthlyRentDeposit = createNumber("monthlyRentDeposit", Integer.class);

    public final NumberPath<Integer> monthlyRentFee = createNumber("monthlyRentFee", Integer.class);

    public final com.househub.backend.domain.property.entity.QProperty property;

    public final NumberPath<Long> salePrice = createNumber("salePrice", Long.class);

    public final DatePath<java.time.LocalDate> startedAt = createDate("startedAt", java.time.LocalDate.class);

    public final EnumPath<com.househub.backend.domain.contract.enums.ContractStatus> status = createEnum("status", com.househub.backend.domain.contract.enums.ContractStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QContract(String variable) {
        this(Contract.class, forVariable(variable), INITS);
    }

    public QContract(Path<? extends Contract> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContract(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContract(PathMetadata metadata, PathInits inits) {
        this(Contract.class, metadata, inits);
    }

    public QContract(Class<? extends Contract> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.househub.backend.domain.agent.entity.QAgent(forProperty("agent"), inits.get("agent")) : null;
        this.customer = inits.isInitialized("customer") ? new com.househub.backend.domain.customer.entity.QCustomer(forProperty("customer"), inits.get("customer")) : null;
        this.property = inits.isInitialized("property") ? new com.househub.backend.domain.property.entity.QProperty(forProperty("property"), inits.get("property")) : null;
    }

}

