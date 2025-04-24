package com.househub.backend.domain.agent.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAgent is a Querydsl query type for Agent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAgent extends EntityPathBase<Agent> {

    private static final long serialVersionUID = 1409812647L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAgent agent = new QAgent("agent");

    public final StringPath contact = createString("contact");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final ListPath<com.househub.backend.domain.customer.entity.Customer, com.househub.backend.domain.customer.entity.QCustomer> customers = this.<com.househub.backend.domain.customer.entity.Customer, com.househub.backend.domain.customer.entity.QCustomer>createList("customers", com.househub.backend.domain.customer.entity.Customer.class, com.househub.backend.domain.customer.entity.QCustomer.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath licenseNumber = createString("licenseNumber");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final QRealEstate realEstate;

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final EnumPath<AgentStatus> status = createEnum("status", AgentStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAgent(String variable) {
        this(Agent.class, forVariable(variable), INITS);
    }

    public QAgent(Path<? extends Agent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAgent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAgent(PathMetadata metadata, PathInits inits) {
        this(Agent.class, metadata, inits);
    }

    public QAgent(Class<? extends Agent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.realEstate = inits.isInitialized("realEstate") ? new QRealEstate(forProperty("realEstate")) : null;
    }

}

