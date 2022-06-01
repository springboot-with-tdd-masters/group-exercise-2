package com.group3.exercise.bankapp.security.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name= "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name="user_id")
    private String userId;

    private String userName;

    @CreationTimestamp
    private LocalDateTime createAt;

    private String password;
}
