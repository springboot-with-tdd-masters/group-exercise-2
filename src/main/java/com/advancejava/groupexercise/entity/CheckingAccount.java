package com.advancejava.groupexercise.entity;

import com.advancejava.groupexercise.constants.TypeEnum;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
@JsonTypeName("checking")
public class CheckingAccount extends Account{

    public CheckingAccount() {
        this.setAcctNumber(generate());
        this.setBalance(100.00);
        this.setMinimumBalance(100.00);
        this.setPenalty(10.00);
        this.setTransactionCharge(1.00);
        this.setInterestCharge(0.00);
        this.setType(String.valueOf(TypeEnum.checking));
    }
}
