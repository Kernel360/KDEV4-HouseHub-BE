package com.househub.backend.domain.sms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSms is a Querydsl query type for Sms
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSms extends EntityPathBase<Sms> {

    private static final long serialVersionUID = 317672871L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSms sms = new QSms("sms");

    public final com.househub.backend.domain.agent.entity.QAgent agent;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath msg = createString("msg");

    public final EnumPath<com.househub.backend.domain.sms.enums.MessageType> msgType = createEnum("msgType", com.househub.backend.domain.sms.enums.MessageType.class);

    public final StringPath rdate = createString("rdate");

    public final StringPath receiver = createString("receiver");

    public final StringPath rtime = createString("rtime");

    public final StringPath sender = createString("sender");

    public final EnumPath<com.househub.backend.domain.sms.enums.SmsStatus> status = createEnum("status", com.househub.backend.domain.sms.enums.SmsStatus.class);

    public final NumberPath<Long> templateId = createNumber("templateId", Long.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QSms(String variable) {
        this(Sms.class, forVariable(variable), INITS);
    }

    public QSms(Path<? extends Sms> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSms(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSms(PathMetadata metadata, PathInits inits) {
        this(Sms.class, metadata, inits);
    }

    public QSms(Class<? extends Sms> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.househub.backend.domain.agent.entity.QAgent(forProperty("agent"), inits.get("agent")) : null;
    }

}

