package com.househub.backend.domain.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {

    private static final long serialVersionUID = 787145693L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomer customer = new QCustomer("customer");

    public final NumberPath<Integer> ageGroup = createNumber("ageGroup", Integer.class);

    public final com.househub.backend.domain.agent.entity.QAgent agent;

    public final StringPath contact = createString("contact");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final EnumPath<com.househub.backend.common.enums.Gender> gender = createEnum("gender", com.househub.backend.common.enums.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final StringPath name = createString("name");

    public final EnumPath<com.househub.backend.domain.customer.enums.CustomerStatus> status = createEnum("status", com.househub.backend.domain.customer.enums.CustomerStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QCustomer(String variable) {
        this(Customer.class, forVariable(variable), INITS);
    }

    public QCustomer(Path<? extends Customer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomer(PathMetadata metadata, PathInits inits) {
        this(Customer.class, metadata, inits);
    }

    public QCustomer(Class<? extends Customer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.househub.backend.domain.agent.entity.QAgent(forProperty("agent"), inits.get("agent")) : null;
    }

}

