package com.househub.backend.domain.agent.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRealEstate is a Querydsl query type for RealEstate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRealEstate extends EntityPathBase<RealEstate> {

    private static final long serialVersionUID = 1913897512L;

    public static final QRealEstate realEstate = new QRealEstate("realEstate");

    public final StringPath address = createString("address");

    public final StringPath businessRegistrationNumber = createString("businessRegistrationNumber");

    public final StringPath contact = createString("contact");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath roadAddress = createString("roadAddress");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QRealEstate(String variable) {
        super(RealEstate.class, forVariable(variable));
    }

    public QRealEstate(Path<? extends RealEstate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRealEstate(PathMetadata metadata) {
        super(RealEstate.class, metadata);
    }

}

