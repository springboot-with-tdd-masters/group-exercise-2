package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends CustomEntityAudit{

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @Column(name = "cp_fk")
    private Long author_id;


}
