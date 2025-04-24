package com.househub.backend.domain.consultation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConsultation is a Querydsl query type for Consultation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConsultation extends EntityPathBase<Consultation> {

    private static final long serialVersionUID = -1355043789L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConsultation consultation = new QConsultation("consultation");

    public final com.househub.backend.domain.agent.entity.QAgent agent;

    public final DateTimePath<java.time.LocalDateTime> consultationDate = createDateTime("consultationDate", java.time.LocalDateTime.class);

    public final EnumPath<com.househub.backend.domain.consultation.enums.ConsultationType> consultationType = createEnum("consultationType", com.househub.backend.domain.consultation.enums.ConsultationType.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.househub.backend.domain.customer.entity.QCustomer customer;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.househub.backend.domain.consultation.enums.ConsultationStatus> status = createEnum("status", com.househub.backend.domain.consultation.enums.ConsultationStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QConsultation(String variable) {
        this(Consultation.class, forVariable(variable), INITS);
    }

    public QConsultation(Path<? extends Consultation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConsultation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConsultation(PathMetadata metadata, PathInits inits) {
        this(Consultation.class, metadata, inits);
    }

    public QConsultation(Class<? extends Consultation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.househub.backend.domain.agent.entity.QAgent(forProperty("agent"), inits.get("agent")) : null;
        this.customer = inits.isInitialized("customer") ? new com.househub.backend.domain.customer.entity.QCustomer(forProperty("customer"), inits.get("customer")) : null;
    }

}

