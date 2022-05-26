package com.masters.masters.exercise.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.Entity;
import java.util.Random;

@Entity
@JsonTypeName("checking")
public class CheckingAccount extends Account{

    public CheckingAccount() {
        this.setAcctNumber(String.valueOf(generateRandomNumber()));
        this.setMinimumBalance(100.00);
        this.setPenalty(10.00);
        this.setTransactionCharge(1.00);
        this.setInterestCharge(0.00);
        this.setBalance(100.00);

    }

    private int generateRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(9000000) + 1000000;
    }
}

