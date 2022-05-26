package com.masters.masters.exercise.model;

import java.time.LocalDate;
import java.util.Random;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("interest")
public class InterestAccount extends Account{

	public InterestAccount() {
        this.setAcctNumber(String.valueOf(generateRandomNumber()));
        this.setBalance(0.00);
        this.setMinimumBalance(0.00);
        this.setPenalty(0.00);
        this.setTransactionCharge(0.00);
        this.setInterestCharge(0.03);

    }
	
	private int generateRandomNumber() {
		Random rand = new Random(); 
		return rand.nextInt(9000000) + 1000000; 
	}

}
