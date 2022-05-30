package com.example.groupexercise2.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import com.example.groupexercise2.util.AccountGenerator;

@Entity
public class InterestAccount extends Account {

	@Override
	public String getType() {
		return "interest";
	}

	@Override
	public void initialize(String name) {
		setName(name);
		setMinimumBalance(0d);
		setBalance(getMinimumBalance());
		setPenalty(0d);
		setTransactionCharge(0d);
		setInterestCharge(0.03);
		setAcctNumber(AccountGenerator.generateAccountNumber());
		setCreatedDate(LocalDate.now());		
	}
}
