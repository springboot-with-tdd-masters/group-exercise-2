package com.advancejava.groupexercise.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import javax.persistence.Entity;

import static com.advancejava.groupexercise.helper.RandomNumberGeneratorUtility.generate;

@Data
@Entity
@JsonTypeName("interest")
public class InterestAccount extends Account{

    public InterestAccount() {

        this.setAcctNumber(generate());
        this.setBalance(0.00);
        this.setMinimumBalance(0.00);
        this.setPenalty(0.00);
        this.setTransactionCharge(0.00);
        this.setInterestCharge(0.03);

    }
}
