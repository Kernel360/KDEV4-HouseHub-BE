package com.househub.backend.domain.sms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSmsTemplate is a Querydsl query type for SmsTemplate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSmsTemplate extends EntityPathBase<SmsTemplate> {

    private static final long serialVersionUID = -1223956671L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSmsTemplate smsTemplate = new QSmsTemplate("smsTemplate");

    public final com.househub.backend.domain.agent.entity.QAgent agent;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QSmsTemplate(String variable) {
        this(SmsTemplate.class, forVariable(variable), INITS);
    }

    public QSmsTemplate(Path<? extends SmsTemplate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSmsTemplate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSmsTemplate(PathMetadata metadata, PathInits inits) {
        this(SmsTemplate.class, metadata, inits);
    }

    public QSmsTemplate(Class<? extends SmsTemplate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.househub.backend.domain.agent.entity.QAgent(forProperty("agent"), inits.get("agent")) : null;
    }

}

