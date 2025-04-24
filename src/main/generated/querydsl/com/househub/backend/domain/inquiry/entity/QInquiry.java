package com.househub.backend.domain.inquiry.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiry is a Querydsl query type for Inquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiry extends EntityPathBase<Inquiry> {

    private static final long serialVersionUID = 604179175L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiry inquiry = new QInquiry("inquiry");

    public final ListPath<InquiryAnswer, QInquiryAnswer> answers = this.<InquiryAnswer, QInquiryAnswer>createList("answers", InquiryAnswer.class, QInquiryAnswer.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.househub.backend.domain.customer.entity.QCustomer customer;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.househub.backend.domain.inquiryTemplate.entity.QInquiryTemplate template;

    public QInquiry(String variable) {
        this(Inquiry.class, forVariable(variable), INITS);
    }

    public QInquiry(Path<? extends Inquiry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiry(PathMetadata metadata, PathInits inits) {
        this(Inquiry.class, metadata, inits);
    }

    public QInquiry(Class<? extends Inquiry> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customer = inits.isInitialized("customer") ? new com.househub.backend.domain.customer.entity.QCustomer(forProperty("customer"), inits.get("customer")) : null;
        this.template = inits.isInitialized("template") ? new com.househub.backend.domain.inquiryTemplate.entity.QInquiryTemplate(forProperty("template"), inits.get("template")) : null;
    }

}

