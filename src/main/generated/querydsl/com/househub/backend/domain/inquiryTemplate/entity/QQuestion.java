package com.househub.backend.domain.inquiryTemplate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -1585387424L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QInquiryTemplate inquiryTemplate;

    public final StringPath label = createString("label");

    public final ListPath<String, StringPath> options = this.<String, StringPath>createList("options", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> questionOrder = createNumber("questionOrder", Integer.class);

    public final BooleanPath required = createBoolean("required");

    public final EnumPath<QuestionType> type = createEnum("type", QuestionType.class);

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.inquiryTemplate = inits.isInitialized("inquiryTemplate") ? new QInquiryTemplate(forProperty("inquiryTemplate"), inits.get("inquiryTemplate")) : null;
    }

}

