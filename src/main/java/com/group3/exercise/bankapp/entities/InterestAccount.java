package com.group3.exercise.bankapp.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(InterestAccount.TYPE)
public class InterestAccount extends Account{

    public static final String TYPE = "interest";

    @Override
    public String getType() {
        return TYPE;
    }
}
