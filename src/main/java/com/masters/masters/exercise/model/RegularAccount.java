package com.masters.masters.exercise.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.Entity;

@Entity
@JsonTypeName("regular")
public class RegularAccount extends Account{
    public RegularAccount() {
        super();
        this.setMinimumBalance(500);
        this.setPenalty(10);
        this.setBalance(500);
    }

}
