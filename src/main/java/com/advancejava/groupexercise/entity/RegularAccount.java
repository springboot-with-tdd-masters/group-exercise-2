package com.advancejava.groupexercise.entity;


import com.advancejava.groupexercise.constants.TypeEnum;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
@JsonTypeName("regular")
public class RegularAccount extends Account {

   /* sample extension
   private Double loan;
    public Double getLoan() {return loan; }
    public void setLoan(Double loan) {this.loan = loan; }
    */

    /**
     * Regular Account
     *
     * No interest
     *
     * Minimum/starting balance 500.00
     *
     * Penalty of 10.00 if balance falls below minimum
     */
    public RegularAccount() {
        /*super("1234567",  500.00, 500.00, 10.00, 0.00, 0.03);
        using this will force you to pass param and will cause to write @JsonProperty for each attributes/fields
         */

        this.setAcctNumber(generate());
        this.setBalance(500.00);
        this.setMinimumBalance(500.00);
        this.setPenalty(10.00);
        this.setTransactionCharge(0.00);
        this.setInterestCharge(0.00);
        this.setType(String.valueOf(TypeEnum.regular));

    }
}
