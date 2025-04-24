package com.househub.backend.domain.inquiryTemplate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiryTemplate is a Querydsl query type for InquiryTemplate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiryTemplate extends EntityPathBase<InquiryTemplate> {

    private static final long serialVersionUID = 49822119L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiryTemplate inquiryTemplate = new QInquiryTemplate("inquiryTemplate");

    public final BooleanPath active = createBoolean("active");

    public final com.househub.backend.domain.agent.entity.QAgent agent;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<Question, QQuestion> questions = this.<Question, QQuestion>createList("questions", Question.class, QQuestion.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QInquiryTemplate(String variable) {
        this(InquiryTemplate.class, forVariable(variable), INITS);
    }

    public QInquiryTemplate(Path<? extends InquiryTemplate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiryTemplate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiryTemplate(PathMetadata metadata, PathInits inits) {
        this(InquiryTemplate.class, metadata, inits);
    }

    public QInquiryTemplate(Class<? extends InquiryTemplate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.househub.backend.domain.agent.entity.QAgent(forProperty("agent"), inits.get("agent")) : null;
    }

}

