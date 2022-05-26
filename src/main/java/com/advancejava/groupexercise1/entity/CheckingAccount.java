package com.advancejava.groupexercise1.entity;

import com.advancejava.groupexercise1.constants.TypeEnum;
import com.advancejava.groupexercise1.helper.RandomNumberGeneratorUtility;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.Entity;

@Entity
@JsonTypeName("checking")
public class CheckingAccount extends Account{

    public CheckingAccount() {
        this.setAcctNumber(RandomNumberGeneratorUtility.generate());
        this.setBalance(100.00);
        this.setMinimumBalance(100.00);
        this.setPenalty(10.00);
        this.setTransactionCharge(1.00);
        this.setInterestCharge(0.00);
        this.setType(String.valueOf(TypeEnum.checking));
    }
}
