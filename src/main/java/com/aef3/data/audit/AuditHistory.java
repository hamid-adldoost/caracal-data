package com.aef3.data.audit;

import com.aef3.data.api.DomainEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "audit_history")
@Entity
public class AuditHistory implements DomainEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creator", length = 100)
    private String creator;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "value", length = 1000)
    private String value;
    @Column(name = "entity_type")
    private String entityType;
    @Column(name = "audit_operation")
    private String operation;

    @Override
    public boolean isAuditable() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
