package com.group3.exercise.bankapp.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(RegularAccount.TYPE)
public class RegularAccount extends Account {

    public final static String TYPE = "regular";

    public RegularAccount() {}

    public RegularAccount(String name, String acctNumber) {
        super(name, acctNumber);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
