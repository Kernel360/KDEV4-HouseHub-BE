package com.househub.backend.domain.inquiryTemplate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiryTemplateSharedToken is a Querydsl query type for InquiryTemplateSharedToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiryTemplateSharedToken extends EntityPathBase<InquiryTemplateSharedToken> {

    private static final long serialVersionUID = 121291885L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiryTemplateSharedToken inquiryTemplateSharedToken = new QInquiryTemplateSharedToken("inquiryTemplateSharedToken");

    public final BooleanPath active = createBoolean("active");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath shareToken = createString("shareToken");

    public final QInquiryTemplate template;

    public QInquiryTemplateSharedToken(String variable) {
        this(InquiryTemplateSharedToken.class, forVariable(variable), INITS);
    }

    public QInquiryTemplateSharedToken(Path<? extends InquiryTemplateSharedToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiryTemplateSharedToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiryTemplateSharedToken(PathMetadata metadata, PathInits inits) {
        this(InquiryTemplateSharedToken.class, metadata, inits);
    }

    public QInquiryTemplateSharedToken(Class<? extends InquiryTemplateSharedToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.template = inits.isInitialized("template") ? new QInquiryTemplate(forProperty("template"), inits.get("template")) : null;
    }

}

