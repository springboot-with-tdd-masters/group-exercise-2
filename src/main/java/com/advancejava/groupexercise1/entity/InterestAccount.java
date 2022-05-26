package com.advancejava.groupexercise1.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.Entity;
import java.util.Random;

@Entity
@JsonTypeName("interest")
public class InterestAccount extends Account{

    public InterestAccount() {
        Random random = new Random();
        //System.out.println();
        this.setAcctNumber(String.valueOf(random.nextInt(1000000)));
        this.setBalance(0.00);
        this.setMinimumBalance(0.00);
        this.setPenalty(0.00);
        this.setTransactionCharge(0.00);
        this.setInterestCharge(0.03);

    }
}
