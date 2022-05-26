package com.advancejava.groupexercise.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CustomEntityAudit implements Serializable {

    private static final long serialVersionUID = 143445254L;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    public Date createdDate;

    @LastModifiedDate
    public Date updatedDate;
}
