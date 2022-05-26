package com.example.demo.model;

import org.springframework.data.annotation.CreatedBy;
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

    private static final long serialVersionUID = 143445154L;

    @CreatedDate
    @Column(name = "created_date",  nullable = false, updatable = false)
    public Date createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    public Date updatedDate;
}
