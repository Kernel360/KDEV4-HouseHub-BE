package com.househub.backend.domain.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomerCandidate is a Querydsl query type for CustomerCandidate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomerCandidate extends EntityPathBase<CustomerCandidate> {

    private static final long serialVersionUID = -1865491930L;

    public static final QCustomerCandidate customerCandidate = new QCustomerCandidate("customerCandidate");

    public final StringPath contact = createString("contact");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QCustomerCandidate(String variable) {
        super(CustomerCandidate.class, forVariable(variable));
    }

    public QCustomerCandidate(Path<? extends CustomerCandidate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerCandidate(PathMetadata metadata) {
        super(CustomerCandidate.class, metadata);
    }

}

